package com.chizg.file;

import com.chizg.tools.file.OutFile;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OutFileTest {

    private static String userIds = ""
            +"159801,"
            +"159806,"
            +"159807,"
            +"159808,"
            +"159812,"
            +"159813,"
            +"159815,"
            +"159816,"
            +"159873,"
            +"159881,"
            +"159898,"
            +"159903,"
            +"159904,"
            +"159911,"
            +"159913,"
            +"159914,"
            +"159916,"
            +"159918,"
            +"159920,"
            +"159921,"
            +"159925,"
            +"159928,"
            +"159929,"
            +"159952,"
            +"159956,"
            +"159963,"
            +"159964,"
            +"159965,"
            +"159966,"
            +"159975,"
            +"159979,"
            +"159980,"
            +"159982,"
            +"159983,"
            +"159985,"
            +"159986,"
            +"159987,"
            +"159990,"
            +"159992,"
            +"159993,"
            +"159995,"
            +"160001,"
            +"160002,"
            +"160003,"
            +"160004,"
            +"160005,"
            +"160024,"
            +"160025,"
            +"160027";
    @Test
    public void toFile01() throws IOException  {
        String fileName = "sqlBack.sql" ;
        String format = "mysqldump -hspiderpool-database-prd-cluster.cluster-c6njpnd312a5.us-west-1.rds.amazonaws.com -uadmin -pJqbkhp05fTv3xQmyqcnR yptpool %s > %s.sql ;\n " ;
        List<String> tables = new ArrayList<>();
        tables.add("job_status_trace_log");
        tables.add("yp_pool_pplns_clear_bak_20230823");
        tables.add("yp_pool_coin_theory_height");
        tables.add("yp_pool_one_block_detail");
        tables.add("yp_pool_user_profit_day_bak_20230823");
        tables.add("yp_pool_found_block_bak_20230823");
        tables.add("yp_pool_worker_day_total_statistics");
        tables.add("JOB_EXECUTION_LOG");
        tables.add("yp_pool_batch_payment_bak_20230823");
        tables.add("yp_pool_user_transfer_accounts");
        tables.add("yp_pool_stat_user_minute_bak_20230823");
        tables.add("yp_pool_user_payment_bak_20230823");
        tables.add("yp_pool_pps_user_clear_bak_20230823");
        tables.add("yp_pool_user_pplns_clear_bak_20230823");
        tables.add("yp_pool_worker_bak_20230823");
        tables.add("yp_pool_saler_customer_profit_day");
        tables.add("temp_user_total_usdt");
        tables.add("f_user_sub_account_log_bak_20230823");
        tables.add("job_execution_log");
        tables.add("yp_pool_proxyer_customer_profit_day");
        tables.add("yp_pool_batch_payment_main_bak_20230823");
        tables.add("f_user_sub_account");
        tables.add("yp_pool_btccom_earn_history");
        tables.add("yp_pool_txbroadcast_txhash_insert");
        tables.add("JOB_STATUS_TRACE_LOG");
        tables.add("yp_pool_txbroadcast_company_transaction");
        tables.add("yp_pool_user_chizg_20220725_bak");
        tables.add("yp_pool_user_chizg_20220725");
        tables.add("yp_pool_user_bak_20230823");
        tables.add("yp_pool_coin_round_bak_20230823");
        tables.add("yp_pool_eth_spider_internal_transaction_mev");
        tables.add("yp_pool_proxyer_profit_day");
        tables.add("yp_pool_eth_found_block");
        tables.add("yp_pool_pps_clear_batch_bak_20230823");
        OutFile.toFile(fileName,format,tables);
    }

    @Test
    public void cearteSql(){

        String sql = "SELECT user_id,FROM_UNIXTIME(`minute`) as '时间',share_accept as '算力',earn_double as '收益' FROM yp_pool_stat_user_minute_coin_fpps WHERE user_id=%s;";

        String[] split = userIds.split(",");
        for (String userId : split){
            String format = String.format(sql, userId);
            System.out.println(format);
        }

    }

    @Test
    public void cearteSql3(){

        String sql = "='%s'!I4";
        String[] split = userIds.split(",");
        for (String userId : split){
            String format = String.format(sql, userId);
            System.out.println(format);
        }

    }
}
