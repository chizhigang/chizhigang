//package com.chizg.tools.hashrate;
//
//import com.ypt.pool.common.constant.Coin;
//import com.ypt.pool.common.constant.JobConstant;
//import com.ypt.pool.common.constant.TelegramConstant;
//import com.ypt.pool.common.dto.TelegramMessageDTO;
//import com.ypt.pool.common.dto.TwoTuple;
//import com.ypt.pool.common.entity.YpPoolStatPoolMinute;
//import com.ypt.pool.common.entity.YpPoolStatUserMinute;
//import com.ypt.pool.common.entity.YpPoolStatWorkerMinute;
//import com.ypt.pool.common.entity.YpPoolSyncBatch;
//import com.ypt.pool.common.enums.CoinAllocationMode;
//import com.ypt.pool.common.util.DateTimeUtil;
//import com.ypt.pool.common.util.HashRateUtil;
//import com.ypt.pool.common.util.HttpclientUtil;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.net.InetAddress;
//import java.util.*;
//
//public class LoadlocalFileDataToMysqllAll {
//
//
//
//
//    private TwoTuple<Boolean, String> readShareFile(File file,
//                                                    YpPoolStatPoolMinute poolPPSPlusMinute, YpPoolStatPoolMinuteCoinPplns poolPPLNSMinute, YpPoolStatPoolMinuteCoinSolo poolSoloMinute, YpPoolStatPoolMinuteCoinFPPS poolFPPMinute,
//                                                    Map<String, YpPoolStatUserMinute> userPPSPlusMap, Map<String, YpPoolStatUserMinuteCoinPplns> userPPLNSMap, Map<String, YpPoolStatUserMinuteCoinSolo> userSoloMap, Map<String, YpPoolStatUserMinuteCoinFPPS> userFPPSMap,
//                                                    Map<String, YpPoolStatWorkerMinute> workersMap) {
//        String fileName = file.getAbsolutePath();
//
//        FileReader fileReader = null;
//        BufferedReader bufferedReader = null;
//
//        if (!file.exists()) {
//            return new TwoTuple<>(true, "succes");
//        }
//
//        try {
//            fileReader = new FileReader(file);
//            bufferedReader = new BufferedReader(fileReader);
//
//            Set<String> hashrateSet = new HashSet<>();
//
//            String line = null;
//            while (true) {
//                line = bufferedReader.readLine();
//                if (line == null) {
//                    break;
//                }
//                hashrateSet.add(line);
//
//                if (hashrateSet.size() >= max_read_line_number) {
//                    this.sumData(hashrateSet,
//                            poolPPSPlusMinute, poolPPLNSMinute, poolSoloMinute, poolFPPMinute,
//                            userPPSPlusMap, userPPLNSMap, userSoloMap, userFPPSMap,
//                            workersMap
//                    );
//                    //10万条数据去重
//                    hashrateSet.clear();
//                }
//            }
//
//            this.sumData(hashrateSet,
//                    poolPPSPlusMinute, poolPPLNSMinute, poolSoloMinute, poolFPPMinute,
//                    userPPSPlusMap, userPPLNSMap, userSoloMap, userFPPSMap,
//                    workersMap
//            );
//            hashrateSet.clear();
//
//            return new TwoTuple<>(true, "success");
//
//        } catch (Exception e) {
//            logger.error("read file=" + fileName + " error," + e.getMessage(), e);
//            return new TwoTuple<Boolean, String>(false, "read fileError " + e.getMessage());
//        } finally {
//            if (bufferedReader != null) {
//                try {
//                    bufferedReader.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            if (fileReader != null) {
//                try {
//                    fileReader.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//
//    private void sumData(Set<String> hashrateSet,
//                         YpPoolStatPoolMinute poolPPSPlusMinute, YpPoolStatPoolMinuteCoinPplns poolPPLNSMinute, YpPoolStatPoolMinuteCoinSolo poolSoloMinute, YpPoolStatPoolMinuteCoinFPPS poolFPPMinute,
//                         Map<String, YpPoolStatUserMinute> userPPSPlusMap, Map<String, YpPoolStatUserMinuteCoinPplns> userPPLNSMap, Map<String, YpPoolStatUserMinuteCoinSolo> userSoloMap, Map<String, YpPoolStatUserMinuteCoinFPPS> userFPPSMap,
//                         Map<String, YpPoolStatWorkerMinute> workersMap) {
//        // TODO 二次去重，发现使用时间戳去重不合理
//        Set<String> hashrateSecondSet = new HashSet<>();
//        for (String share : hashrateSet) {
//            // shares:区块奖励:全网难度:wokername:时间
//            // 0.00917237:187200000000:3001018.418814371:1GzJDgsKqaQnMGuehaFLyaSFNEFNSTnbfC.win11:1517822071695
//            String[] values = share.split(":");
//            if (values.length < 5) {
//                continue;
//            }
//            if (values.length == 6) {
//                String hashrateSecond = share.substring(0, share.lastIndexOf(":"));
//                if (!hashrateSecondSet.add(hashrateSecond)) {
//                    logger.error("data duplication share={},hashrateSecond is {}", share, hashrateSecond);
//                    continue;
//                }
//            }
//            // 区块难度
//            double difficulty = Double.valueOf(values[0]);
//            // 区块奖励
//            long coinbaseReward = difficulty > 0 ? Long.valueOf(values[1]) : 0;
//            // 全网难度
//            double networkDiff = difficulty > 0 ? Double.valueOf(values[2]) : 0;
//            // 子账号.机器编号
//            String workName = values[3].trim();
//            TwoTuple<String, String> userNames = HashRateUtil.getUserName(coin_, workName);
//            String user = userNames.first;
//            String workSimpleName = userNames.second;
//            String workFullName = user + "." + workSimpleName;
//            // 提交的时间
//            long time = Long.valueOf(values[values.length - 1]) / 1000;
//
//            // 从内存中获取每个用户的结算模式
//            CoinAllocationMode mode = CoinAllocationMode.getModeByCode(CacheUtil.get(user));
//            if (null == mode) {
//                mode = CoinAllocationMode.FPPS;
//            }
//
//            if (CoinAllocationMode.PPLNS.equals(mode)) {
//
//                // 矿机 更新最后提交时间
//                YpPoolStatWorkerMinute worker = workersMap.get(workFullName);
//                if (null == worker) {
//                    worker = new YpPoolStatWorkerMinute(userNames);
//                    workersMap.put(workFullName, worker);
//                }
//                if (worker.getLastShareTime() < (int) time) {
//                    worker.setLastShareTime(time);
//                }
//
//                // 子账号
//                YpPoolStatUserMinuteCoinPplns userPPLNS = userPPLNSMap.get(user);
//                if (null == userPPLNS) {
//                    userPPLNS = new YpPoolStatUserMinuteCoinPplns();
//                    userPPLNSMap.put(user, userPPLNS);
//                }
//
//                if (difficulty < 0) {
//                    //无效share
//                    poolPPLNSMinute.setShareReject(poolPPLNSMinute.getShareReject().add(BigDecimal.valueOf(-1 * difficulty)));
//                    userPPLNS.setShareReject(userPPLNS.getShareReject().add(BigDecimal.valueOf(-1 * difficulty)));
//                } else {
//                    //有效share
//                    poolPPLNSMinute.setShareAccept(poolPPLNSMinute.getShareAccept().add(BigDecimal.valueOf(difficulty)));
//                    userPPLNS.setShareAccept(userPPLNS.getShareAccept().add(BigDecimal.valueOf(difficulty)));
//
//                    if (networkDiff > 0) {
//                        double score = difficulty / networkDiff;
//                        if (difficulty >= networkDiff) {
//                            score = 1.0;
//                        }
//                        poolPPLNSMinute.setScore(poolPPLNSMinute.getScore().add(BigDecimal.valueOf(score)));
//                        userPPLNS.setScore(userPPLNS.getScore().add(BigDecimal.valueOf(score)));
//
//                        // earn
//                        double earnDouble = coinbaseReward * score;
//                        BigDecimal earnBigDecimal = new BigDecimal(coinbaseReward + "").multiply(new BigDecimal(score + ""));
//                        if (Coin.btc.equals(coin_)) {
//                            // BTC 保留聪后面12位
//                            earnBigDecimal.setScale(12, RoundingMode.DOWN);
//                        }
//                        poolPPLNSMinute.setEarnDouble(poolPPLNSMinute.getEarnDouble().add(earnBigDecimal));
//                        userPPLNS.setEarnDouble(userPPLNS.getEarnDouble().add(earnBigDecimal));
//
//                        long earn = (long) (earnDouble);
//                        poolPPLNSMinute.setEarn(poolPPLNSMinute.getEarn() + earn);
//                        userPPLNS.setEarn(userPPLNS.getEarn() + earn);
//                    }
//                }
//            } else if (CoinAllocationMode.SOLO.equals(mode)) {
//
//                // 矿机 更新最后提交时间
//                YpPoolStatWorkerMinute worker = workersMap.get(workFullName);
//                if (null == worker) {
//                    worker = new YpPoolStatWorkerMinute(userNames);
//                    workersMap.put(workFullName, worker);
//                }
//                if (worker.getLastShareTime() < (int) time) {
//                    worker.setLastShareTime(time);
//                }
//
//                // 子账号
//                YpPoolStatUserMinuteCoinSolo userSolo = userSoloMap.get(user);
//                if (null == userSolo) {
//                    userSolo = new YpPoolStatUserMinuteCoinSolo();
//                    userSoloMap.put(user, userSolo);
//                }
//
//                if (difficulty < 0) {
//                    //无效share
//                    poolSoloMinute.setShareReject(poolSoloMinute.getShareReject().add(BigDecimal.valueOf(-1 * difficulty)));
//                    userSolo.setShareReject(userSolo.getShareReject().add(BigDecimal.valueOf(-1 * difficulty)));
//                } else {
//                    //有效share
//                    poolSoloMinute.setShareAccept(poolSoloMinute.getShareAccept().add(BigDecimal.valueOf(difficulty)));
//                    userSolo.setShareAccept(userSolo.getShareAccept().add(BigDecimal.valueOf(difficulty)));
//
//                    if (networkDiff > 0) {
//                        double score = difficulty / networkDiff;
//                        if (difficulty >= networkDiff) {
//                            score = 1.0;
//                        }
//                        poolSoloMinute.setScore(poolSoloMinute.getScore().add(BigDecimal.valueOf(score)));
//                        userSolo.setScore(userSolo.getScore().add(BigDecimal.valueOf(score)));
//
//                        // earn
//                        double earnDouble = coinbaseReward * score;
//                        BigDecimal earnBigDecimal = new BigDecimal(coinbaseReward + "").multiply(new BigDecimal(score + ""));
//                        if (Coin.btc.equals(coin_)) {
//                            earnBigDecimal.setScale(12, RoundingMode.DOWN);
//                        }
//                        poolSoloMinute.setEarnDouble(poolSoloMinute.getEarnDouble().add(earnBigDecimal));
//                        userSolo.setEarnDouble(userSolo.getEarnDouble().add(earnBigDecimal));
//
//                        long earn = (long) (earnDouble);
//                        poolSoloMinute.setEarn(poolSoloMinute.getEarn() + earn);
//                        userSolo.setEarn(userSolo.getEarn() + earn);
//
//                    }
//                }
//            } else if (CoinAllocationMode.PPSPLUS.equals(mode)) {
//                // 矿机 更新最后提交时间
//                YpPoolStatWorkerMinute worker = workersMap.get(workFullName);
//                if (null == worker) {
//                    worker = new YpPoolStatWorkerMinute(userNames);
//                    workersMap.put(workFullName, worker);
//                }
//                if (worker.getLastShareTime() < (int) time) {
//                    worker.setLastShareTime(time);
//                }
//
//                // 子账号
//                YpPoolStatUserMinute userPPSPlus = userPPSPlusMap.get(user);
//                if (userPPSPlus == null) {
//                    userPPSPlus = new YpPoolStatUserMinute();
//                    userPPSPlusMap.put(user, userPPSPlus);
//                }
//
//                // 无效share
//                if (difficulty < 0) {
//                    poolPPSPlusMinute.setShareReject(poolPPSPlusMinute.getShareReject().add(BigDecimal.valueOf(-1 * difficulty)));
//                    userPPSPlus.setShareReject(userPPSPlus.getShareReject().add(BigDecimal.valueOf(-1 * difficulty)));
//                } else {
//                    // 有效share
//                    poolPPSPlusMinute.setShareAccept(poolPPSPlusMinute.getShareAccept().add(BigDecimal.valueOf(difficulty)));
//                    userPPSPlus.setShareAccept(userPPSPlus.getShareAccept().add(BigDecimal.valueOf(difficulty)));
//
//                    if (networkDiff > 0) {
//                        double score = difficulty / networkDiff;
//                        if (difficulty >= networkDiff) {
//                            score = 1.0;
//                        }
//
//                        poolPPSPlusMinute.setScore(poolPPSPlusMinute.getScore().add(BigDecimal.valueOf(score)));
//                        userPPSPlus.setScore(userPPSPlus.getScore().add(BigDecimal.valueOf(score)));
//
//                        // earn
//                        double earnDouble = coinbaseReward * score;
//                        BigDecimal earnBigDecimal = new BigDecimal(coinbaseReward + "").multiply(new BigDecimal(score + ""));
//                        if (Coin.btc.equals(coin_)) {
//                            earnBigDecimal.setScale(12, RoundingMode.DOWN);
//                        }
//                        poolPPSPlusMinute.setEarnDouble(poolPPSPlusMinute.getEarnDouble().add(earnBigDecimal));
//                        userPPSPlus.setEarnDouble(userPPSPlus.getEarnDouble().add(earnBigDecimal));
//
//                        long earn = (long) (earnDouble);
//                        poolPPSPlusMinute.setEarn(poolPPSPlusMinute.getEarn() + earn);
//                        userPPSPlus.setEarn(userPPSPlus.getEarn() + earn);
//                    }
//                }
//            } else if (CoinAllocationMode.FPPS.equals(mode)) {
//                // 矿机 更新最后提交时间
//                YpPoolStatWorkerMinute worker = workersMap.get(workFullName);
//                if (null == worker) {
//                    worker = new YpPoolStatWorkerMinute(userNames);
//                    workersMap.put(workFullName, worker);
//                }
//                if (worker.getLastShareTime() < (int) time) {
//                    worker.setLastShareTime(time);
//                }
//
//                // 子账号
//                YpPoolStatUserMinuteCoinFPPS userFPPS = userFPPSMap.get(user);
//                if (userFPPS == null) {
//                    userFPPS = new YpPoolStatUserMinuteCoinFPPS();
//                    userFPPSMap.put(user, userFPPS);
//                }
//
//                // 无效share
//                if (difficulty < 0) {
//                    poolFPPMinute.setShareReject(poolFPPMinute.getShareReject().add(BigDecimal.valueOf(-1 * difficulty)));
//                    userFPPS.setShareReject(userFPPS.getShareReject().add(BigDecimal.valueOf(-1 * difficulty)));
//                } else {
//                    // 有效share
//                    poolFPPMinute.setShareAccept(poolFPPMinute.getShareAccept().add(BigDecimal.valueOf(difficulty)));
//                    userFPPS.setShareAccept(userFPPS.getShareAccept().add(BigDecimal.valueOf(difficulty)));
//
//                    if (networkDiff > 0) {
//                        double score = difficulty / networkDiff;
//                        if (difficulty >= networkDiff) {
//                            score = 1.0;
//                        }
//
//                        poolFPPMinute.setScore(poolFPPMinute.getScore().add(BigDecimal.valueOf(score)));
//                        userFPPS.setScore(userFPPS.getScore().add(BigDecimal.valueOf(score)));
//
//                        // earn
//                        double earnDouble = coinbaseReward * score;
//                        BigDecimal earnBigDecimal = new BigDecimal(coinbaseReward + "").multiply(new BigDecimal(score + ""));
//                        if (Coin.btc.equals(coin_)) {
//                            earnBigDecimal.setScale(12, RoundingMode.DOWN);
//                        }
//                        poolFPPMinute.setEarnDouble(poolFPPMinute.getEarnDouble().add(earnBigDecimal));
//                        userFPPS.setEarnDouble(userFPPS.getEarnDouble().add(earnBigDecimal));
//
//                        long earn = (long) (earnDouble);
//                        poolFPPMinute.setEarn(poolFPPMinute.getEarn() + earn);
//                        userFPPS.setEarn(userFPPS.getEarn() + earn);
//                    }
//                }
//            } else {
//                this.sendTelegram("取到分配模式异常", share + "," + mode.getCode());
//                return;
//            }
//
//        }
//    }
//
//
//    private void saveDataToDb(YpPoolStatPoolMinute poolPPSPlusMinute, YpPoolStatPoolMinuteCoinPplns poolPPLNSMinute, YpPoolStatPoolMinuteCoinSolo poolSoloMinute, YpPoolStatPoolMinuteCoinFPPS poolFPPMinute,
//                              Map<String, YpPoolStatUserMinute> userPPSPlusMap, Map<String, YpPoolStatUserMinuteCoinPplns> userPPLNSMap, Map<String, YpPoolStatUserMinuteCoinSolo> userSoloMap, Map<String, YpPoolStatUserMinuteCoinFPPS> userFPPSMap,
//                              Map<String, YpPoolStatWorkerMinute> workerPPSPlusMap,
//                              int startMinute) {
//        // 入库,pool 处理
//        poolPPSPlusMinute.setCoin(this.runCoin);
//        // 变为正数
//        poolPPSPlusMinute.setRejectRate(
//                HashRateUtil.calculateRejectRate(poolPPSPlusMinute.getShareAccept(), poolPPSPlusMinute.getShareReject()));
//        poolPPSPlusMinute.setCreatedAt(new Date());
//        poolPPSPlusMinute.setMinute(startMinute);
//
//        //入库 pplns 处理
//        poolPPLNSMinute.setCoin(this.runCoin);
//        poolPPLNSMinute.setRejectRate(HashRateUtil.calculateRejectRate(poolPPLNSMinute.getShareAccept(), poolPPLNSMinute.getShareReject()));
//        poolPPLNSMinute.setCreatedAt(new Date());
//        poolPPLNSMinute.setMinute(startMinute);
//
//        //入库 solo 处理
//        poolSoloMinute.setCoin(this.runCoin);
//        poolSoloMinute.setRejectRate(HashRateUtil.calculateRejectRate(poolSoloMinute.getShareAccept(), poolSoloMinute.getShareReject()));
//        poolSoloMinute.setCreatedAt(new Date());
//        poolSoloMinute.setMinute(startMinute);
//
//        //入库 fpps 处理
//        poolFPPMinute.setCoin(this.runCoin);
//        poolFPPMinute.setRejectRate(HashRateUtil.calculateRejectRate(poolFPPMinute.getShareAccept(), poolFPPMinute.getShareReject()));
//        poolFPPMinute.setCreatedAt(new Date());
//        poolFPPMinute.setMinute(startMinute);
//
//        Map<String, YpPoolStatWorkerMinute> errorWorkMap = new HashMap<>();
//
//        Map<String, YpPoolStatUserMinute> errorPPSPLUSMap = new HashMap<>();
//        Map<String, YpPoolStatUserMinuteCoinPplns> errorPplnsMap = new HashMap<>();
//        Map<String, YpPoolStatUserMinuteCoinSolo> errorSoloMap = new HashMap<>();
//        Map<String, YpPoolStatUserMinuteCoinFPPS> errorFPPMap = new HashMap<>();
//
//        // user pps+
//        for (Map.Entry<String, YpPoolStatUserMinute> entry : userPPSPlusMap.entrySet()) {
//            YpPoolStatUserMinute user = entry.getValue();
//            user.setCoin(this.runCoin);
//            user.setCreatedAt(new Date());
//            user.setMinute(startMinute);
//            user.setRejectRate(HashRateUtil.calculateRejectRate(user.getShareAccept(), user.getShareReject()));
//
//            Integer userId = userExistMap.get(entry.getKey());
//            if (userId == null) {
//                try {
//                    userId = ypPoolUserService.queryUserIdByUserName(this.runCoin, entry.getKey());
//                    userExistMap.put(entry.getKey(), userId);
//                } catch (Exception e) {
//                    logger.info("异常用户：coin->{},userName->{}", user.getCoin(), user.getUserName());
//                    errorPPSPLUSMap.put(entry.getKey(), entry.getValue());
//                    continue;
//                }
//            }
//
//            user.setUserId(userId);
//        }
//
//        // user pplns
//        for (Map.Entry<String, YpPoolStatUserMinuteCoinPplns> entry : userPPLNSMap.entrySet()) {
//            YpPoolStatUserMinuteCoinPplns userPplns = entry.getValue();
//            userPplns.setCoin(this.runCoin);
//            userPplns.setCreatedAt(new Date());
//            userPplns.setMinute(startMinute);
//            userPplns.setRejectRate(HashRateUtil.calculateRejectRate(userPplns.getShareAccept(), userPplns.getShareReject()));
//
//            Integer userId = userExistMap.get(entry.getKey());
//            if (userId == null) {
//                try {
//                    userId = ypPoolUserService.queryUserIdByUserName(this.runCoin, entry.getKey());
//                    userExistMap.put(entry.getKey(), userId);
//                } catch (Exception e) {
//                    logger.info("异常用户：coin->{},userName->{}", userPplns.getCoin(), userPplns.getUserName());
//                    errorPplnsMap.put(entry.getKey(), entry.getValue());
//                    continue;
//                }
//            }
//
//            userPplns.setUserId(userId);
//
//        }
//
//        // user solo
//        for (Map.Entry<String, YpPoolStatUserMinuteCoinSolo> entry : userSoloMap.entrySet()) {
//            YpPoolStatUserMinuteCoinSolo userSolo = entry.getValue();
//            userSolo.setCoin(this.runCoin);
//            userSolo.setCreatedAt(new Date());
//            userSolo.setMinute(startMinute);
//            userSolo.setRejectRate(HashRateUtil.calculateRejectRate(userSolo.getShareAccept(), userSolo.getShareReject()));
//
//            Integer userId = userExistMap.get(entry.getKey());
//            if (userId == null) {
//                try {
//                    userId = ypPoolUserService.queryUserIdByUserName(this.runCoin, entry.getKey());
//                    userExistMap.put(entry.getKey(), userId);
//                } catch (Exception e) {
//                    logger.info("异常用户：coin->{},userName->{}", userSolo.getCoin(), userSolo.getUserName());
//                    errorSoloMap.put(entry.getKey(), entry.getValue());
//                    continue;
//                }
//            }
//
//            userSolo.setUserId(userId);
//
//        }
//
//        // user fpps
//        for (Map.Entry<String, YpPoolStatUserMinuteCoinFPPS> entry : userFPPSMap.entrySet()) {
//            YpPoolStatUserMinuteCoinFPPS userFPPS = entry.getValue();
//            userFPPS.setCoin(this.runCoin);
//            userFPPS.setCreatedAt(new Date());
//            userFPPS.setMinute(startMinute);
//            userFPPS.setRejectRate(HashRateUtil.calculateRejectRate(userFPPS.getShareAccept(), userFPPS.getShareReject()));
//
//            Integer userId = userExistMap.get(entry.getKey());
//            if (userId == null) {
//                try {
//                    userId = ypPoolUserService.queryUserIdByUserName(this.runCoin, entry.getKey());
//                    userExistMap.put(entry.getKey(), userId);
//                } catch (Exception e) {
//                    logger.info("异常用户：coin->{},userName->{}", userFPPS.getCoin(), userFPPS.getUserName());
//                    errorFPPMap.put(entry.getKey(), entry.getValue());
//                    continue;
//                }
//            }
//            userFPPS.setUserId(userId);
//        }
//
//        for (String key : errorPPSPLUSMap.keySet()) {
//            userPPSPlusMap.remove(key);
//        }
//        for (String key : errorPplnsMap.keySet()) {
//            userPPLNSMap.remove(key);
//        }
//        for (String key : errorSoloMap.keySet()) {
//            userSoloMap.remove(key);
//        }
//        for (String key : errorFPPMap.keySet()) {
//            userFPPSMap.remove(key);
//        }
//
//        for (Map.Entry<String, YpPoolStatWorkerMinute> entry : workerPPSPlusMap.entrySet()) {
//            TwoTuple<String, String> userNames = entry.getValue().getUserNames();
//            String user = userNames.first;
//            String workSimpleName = userNames.second;
//            String workFullName = user + "." + workSimpleName;
//
//            Integer userId = userExistMap.get(user);
//            Integer workerId = workerExistMap.get(entry.getKey());
//
//            if (workerId == null) {
//                try {
//                    workerId = ypPoolWorkerService.queryWorkerIdByUserName(this.runCoin, user,
//                            workSimpleName, userId);
//                    workerExistMap.put(workFullName, workerId);
//                } catch (Exception e) {
//                    logger.info("跳过异常矿机：userName： {}，workerName：{}", user, workSimpleName);
//                    errorWorkMap.put(entry.getKey(), entry.getValue());
//                    continue;
//                }
//            }
//            entry.getValue().setWorkerId(workerId);
//            entry.getValue().setUserId(userId);
//        }
//
//        // 入库
//        ypPoolStatService.saveStatData(userPPSPlusMap, poolPPSPlusMinute,
//                userPPLNSMap, poolPPLNSMinute,
//                userSoloMap, poolSoloMinute,
//                userFPPSMap, poolFPPMinute);
//
//        try {
//            // work数据 -不影响用户的数据
//            ypPoolStatService.refreshWorkData(workerPPSPlusMap, runCoin, startMinute * 1l);
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//        }
//
//    }
//
//
//}
