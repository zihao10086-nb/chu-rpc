package com.qiaochu.churpc.registry;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.json.JSONUtil;
import com.qiaochu.churpc.config.RegistryConfig;
import com.qiaochu.churpc.model.ServiceMetaInfo;
import io.etcd.jetcd.*;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import io.etcd.jetcd.watch.WatchEvent;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Etcd注册中心
 */
@Slf4j
public class EtcdRegistry implements Registry {

    private Client client;

    private KV kvClient;
    /**
     * 注册中心服务缓存
     */
    private final RegistryServiceCache registryServiceCache = new RegistryServiceCache();
    /**
     * 正在监听的key集合
     */
    private final Set<String> watchingKeySet=new ConcurrentHashSet<>();
    /**
     * 本机注册的节点 key 集合（用于维护续期）
     */
    private final Set<String> localRegisterNodeKeySet = new HashSet<>();

    /**
     * 根节点路径
     */
    private static final String ETCD_ROOT_PATH = "/churpc/";

    @Override
    public void init(RegistryConfig registryConfig) {
        client = Client.builder()
                .endpoints(registryConfig.getAddress())
                .connectTimeout(Duration.ofMillis(registryConfig.getTimeout()))
                .build();
        kvClient = client.getKVClient();
        heartBeat();
    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) throws Exception {
        //创建lease 和 KV 客户端
        Lease leaseClient = client.getLeaseClient();
        //创建租约
        long leaseId = leaseClient.grant(30).get().getID();
        //设置要存储的键值对
        String registryKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(registryKey, StandardCharsets.UTF_8);
        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo), StandardCharsets.UTF_8);
        //将键值与租约关联起来
        PutOption putOption=PutOption.builder().withLeaseId(leaseId).build();
        kvClient.put(key, value, putOption).get();
        //添加节点信息到本地存储
        localRegisterNodeKeySet.add(registryKey);
    }

    @Override
    public void unRegister(ServiceMetaInfo serviceMetaInfo) {
        try {
            String registryKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
            kvClient.delete(ByteSequence.from(ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey(), StandardCharsets.UTF_8)).get();
            localRegisterNodeKeySet.remove(registryKey);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {

        //优先从缓存中获取
        List<ServiceMetaInfo> serviceMetaInfoList = registryServiceCache.readCache();
        if (serviceMetaInfoList!=null){
            return serviceMetaInfoList;
        }

        //前缀搜索，结尾一定要加“/”
        String searchPrefix = ETCD_ROOT_PATH + serviceKey + "/";
        try {
            //前缀查询
            GetOption getOption = GetOption.builder().isPrefix(true).build();
            List<KeyValue> keyValues=kvClient.get(
                    ByteSequence.from(searchPrefix, StandardCharsets.UTF_8),
                    getOption)
                    .get()
                    .getKvs();
            //解析服务信息
            List<ServiceMetaInfo> serviceMetaInfos = keyValues.stream()
                    .map(keyValue -> {
                        //监听key变化
                        String key = keyValue.getKey().toString(StandardCharsets.UTF_8);
                        watch(key);
                        String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        return JSONUtil.toBean(value, ServiceMetaInfo.class);
                    }).collect(Collectors.toList());
            registryServiceCache.writeCache(serviceMetaInfos);
            log.info("服务列表：{}",serviceMetaInfos);
            return serviceMetaInfos;
        } catch (Exception e) {
            throw new RuntimeException("获取服务列表失败",e);
        }
    }

    @Override
    public void destroy() {
        System.out.println("当前节点下线");

        //下线节点
        //遍历本节点所有key
        for (String registryKey : localRegisterNodeKeySet){
            try {
                kvClient.delete(ByteSequence.from(registryKey, StandardCharsets.UTF_8)).get();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        //释放资源
        if (kvClient!= null){
            kvClient.close();
        }
        if (client!= null){
            client.close();
        }
    }

    @Override
    public void heartBeat() {
        CronUtil.schedule("*/10 * * * * *", new Task() {
            @Override
            public void execute() {
                for (String registryKey : localRegisterNodeKeySet){
                    try {
                        List<KeyValue> values = kvClient.get(ByteSequence.from(registryKey, StandardCharsets.UTF_8))
                                .get()
                                .getKvs();
                        //该节点已过期
                        if (CollUtil.isEmpty(values)){
                            continue;
                        }
                        //节点未过期
                        KeyValue keyValue = values.get(0);
                        String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        ServiceMetaInfo bean = JSONUtil.toBean(value, ServiceMetaInfo.class);
                        //续约
                        register(bean);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        //支持秒级别定时任务
        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }

    @Override
    public void watch(String serviceNodeKey) {
        Watch watchClient = client.getWatchClient();
        //之前未被监听，开启监听
        boolean newWatch = watchingKeySet.add(serviceNodeKey);
        if (newWatch){
            watchClient.watch(ByteSequence.from(serviceNodeKey, StandardCharsets.UTF_8),response -> {
                for (WatchEvent event : response.getEvents()){
                    switch (event.getEventType()){
                        case DELETE:
                            registryServiceCache.clearCache();
                            break;
                        case PUT:
                        default:
                            break;
                    }
                }
            });
        }

    }
}
