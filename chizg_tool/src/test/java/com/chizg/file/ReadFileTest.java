package com.chizg.file;

import com.chizg.tools.vo.UserFee;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ReadFileTest {

    @Test
    public void readData20() {
        String sql = "UPDATE `yptpool`.`yp_pool_user` SET `user_available_money` = `user_available_money` %s, `user_total_money` = `user_total_money` %s WHERE `user_id` = %s ;";

        String sql02 = "UPDATE `yp_pool_user_profit_day` SET `avg_share_accept` = `avg_share_accept` %s, `day_profit` = `day_profit` %s , `total_profit` = `total_profit` %s  WHERE  `user_id` = %s and `day` = 1710979200;";

        // user fee
        Map<Integer, UserFee> userFeeMap = new HashMap<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get("E:\\develop\\github\\chizhigang\\data\\user_fee.csv"))) {
            // CSV文件的分隔符
            String DELIMITER = ",";
            // 按行读取
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                // 分割
                String[] columns = line.split(DELIMITER);
                // 打印行
                Integer userId = Integer.parseInt(columns[0]);
                String ppsFee = columns[1];
                String pplnsFee = columns[2];
                UserFee userFee = new UserFee(userId, new BigDecimal(ppsFee), new BigDecimal(pplnsFee));
                userFeeMap.put(userId, userFee);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // 创建 reader
        try (BufferedReader br = Files.newBufferedReader(Paths.get("E:\\develop\\github\\chizhigang\\data\\20240322.csv"))) {
            // CSV文件的分隔符
            String DELIMITER = ",";
            // 按行读取
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                // 分割
                String[] columns = line.split(DELIMITER);
                // 打印行
                String ppsMoney = columns[1];
                String userId = columns[0];
//                if (columns[1].indexOf("-") > -1) {
//                    ppsMoney = columns[1];
//                } else {
//                    ppsMoney = "+" + columns[1];
//                }
                //
                UserFee userFee = userFeeMap.get(new Integer(userId));

                // userPPS 佩服金额
                BigDecimal userPPSMoney = new BigDecimal(ppsMoney).multiply(BigDecimal.ONE.subtract(userFee.getPpsFee()));

//                String format = String.format(sql, money, money, userId);
//                System.out.println(format);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void readData20240322() {
        String sql = "UPDATE `yp_pool_user_profit_day` SET `avg_share_accept` = `avg_share_accept` %s, `day_profit` = `day_profit` %s , `total_profit` = `total_profit` %s  WHERE  `user_id` = %s and `day` = 1710979200;";

        BigDecimal sum = BigDecimal.ZERO ;
        // 创建 reader
        try (BufferedReader br = Files.newBufferedReader(Paths.get("E:\\develop\\github\\chizhigang\\data\\userMoney.csv"))) {
            // CSV文件的分隔符
            String DELIMITER = ",";
            // 按行读取
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                // 分割
                String[] columns = line.split(DELIMITER);
                // 打印行
                String hash = new BigDecimal(columns[1].replace("`","")).setScale(0,RoundingMode.DOWN).toPlainString();
                String money = new BigDecimal(columns[2].replace("`","")).setScale(20, RoundingMode.DOWN).toPlainString();
                String userId = columns[0];
                sum = sum.add(new BigDecimal(money));
                if (money.indexOf("-") == 0) {
                    money =money;
                } else {
                    money = "+" + money;
                }
                if (hash.indexOf("-") == 0) {
                    hash =hash;
                } else {
                    hash = "+" + hash;
                }
                String format = String.format(sql,hash, money, money, userId);
                System.out.println(format);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("sum="+sum.toPlainString());
    }

    @Test
    public void readData20240322ProfitDay() {
        String sql = "UPDATE `yptpool`.`yp_pool_user` SET `user_available_money` = `user_available_money` %s, `user_total_money` = `user_total_money` %s WHERE `user_id` = %s ;";

        BigDecimal sum = BigDecimal.ZERO ;
        // 创建 reader
        try (BufferedReader br = Files.newBufferedReader(Paths.get("E:\\develop\\github\\chizhigang\\data\\userMoney.csv"))) {
            // CSV文件的分隔符
            String DELIMITER = ",";
            // 按行读取
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                // 分割
                String[] columns = line.split(DELIMITER);
                // 打印行
                String money = new BigDecimal(columns[1].replace("`","")).setScale(20, RoundingMode.DOWN).toPlainString();
                String userId = columns[0];
                sum = sum.add(new BigDecimal(money));
                if (money.indexOf("-") == 0) {
                    money =money;
                } else {
                    money = "+" + money;
                }
                String format = String.format(sql, money, money, userId);
                System.out.println(format);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("sum="+sum.toPlainString());
    }

    @Test
    public void readData20240322ProfitDayUserPPSAndPPLNS() {
//        String sql = "UPDATE `yp_pool_user_profit_day` SET `avg_share_accept` = `avg_share_accept` %s, `day_profit` = `day_profit` %s , `total_profit` = `total_profit` %s  WHERE  `user_id` = %s and `day` = 1710979200;";
        String sql = "UPDATE `yp_pool_user_profit_day` SET `pps_day_profit` = `pps_day_profit` %s, `pplns_day_profit` = `pplns_day_profit` %s , " +
                "`pps_day_pool_real_profit` = `pps_day_pool_real_profit` %s,`pplns_day_pool_profit`=`pplns_day_pool_profit` %s  WHERE  `user_id` = %s and `day` = 1710979200;";

        BigDecimal sumPplns = BigDecimal.ZERO ;
        BigDecimal sumPps = BigDecimal.ZERO ;
        BigDecimal sumPoolPps = BigDecimal.ZERO ;
        BigDecimal sumPoolPplns = BigDecimal.ZERO ;
        // 创建 reader
        try (BufferedReader br = Files.newBufferedReader(Paths.get("E:\\develop\\github\\chizhigang\\data\\userPPSandPPLNS.csv"))) {
            // CSV文件的分隔符
            String DELIMITER = ",";
            // 按行读取
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                // 分割
                String[] columns = line.split(DELIMITER);
                // user_id,pps,pplns,pool-pps,pool-pplns
                String pps = new BigDecimal(columns[1].replace("`","")).setScale(20,RoundingMode.DOWN).toPlainString();
                String pplns = new BigDecimal(columns[2].replace("`","")).setScale(20, RoundingMode.DOWN).toPlainString();
                String poolPps = new BigDecimal(columns[3].replace("`","")).setScale(20, RoundingMode.DOWN).toPlainString();
                String poolPplns = new BigDecimal(columns[4].replace("`","")).setScale(20, RoundingMode.DOWN).toPlainString();
                String userId = columns[0];
                sumPps = sumPps.add(new BigDecimal(pps));
                sumPplns = sumPplns.add(new BigDecimal(pplns));
                sumPoolPps = sumPoolPps.add(new BigDecimal(poolPps));
                sumPoolPplns = sumPoolPplns.add(new BigDecimal(poolPplns));
                if (pplns.indexOf("-") == 0) {
                    pplns =pplns;
                } else {
                    pplns = "+" + pplns;
                }
                if (pps.indexOf("-") == 0) {
                    pps =pps;
                } else {
                    pps = "+" + pps;
                }
                if (poolPps.indexOf("-") == 0) {
                    poolPps =poolPps;
                } else {
                    poolPps = "+" + poolPps;
                }
                if (poolPplns.indexOf("-") == 0) {
                    poolPplns =poolPplns;
                } else {
                    poolPplns = "+" + poolPplns;
                }
                String format = String.format(sql,pps, pplns, poolPps,poolPplns, userId);
                System.out.println(format);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("sumPps="+sumPps.toPlainString());
        System.out.println("sumPplns="+sumPplns.toPlainString());
        System.out.println("sumPoolPps="+sumPoolPps.toPlainString());
        System.out.println("sumPoolPplns="+sumPoolPplns.toPlainString());
    }

    @Test
    public void aa(){
        System.out.println( "456-asfa".indexOf("-"));
    }
}
