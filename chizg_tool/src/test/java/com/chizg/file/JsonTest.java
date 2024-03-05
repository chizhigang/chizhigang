package com.chizg.file;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Iterator;

public class JsonTest {

    @Test
    public void readJsonFile() throws IOException {
        File file=new File("E:\\develop\\github\\chizhigang\\data\\yp_pool_btc_transaction_record_bak20231114.json");
        String content= FileUtils.readFileToString(file,"UTF-8");
        JSONObject jsonObject=new JSONObject(content);

        // 支付总收益
        File sunPaymentMoneyFile=new File("E:\\develop\\github\\chizhigang\\data\\aaaaa.json");
        String sunPaymentMoneyContent= FileUtils.readFileToString(sunPaymentMoneyFile,"UTF-8");
        JSONObject sunPaymentMoneyJsonObject=new JSONObject(sunPaymentMoneyContent);

        JSONArray data = jsonObject.getJSONArray("data");
        int length = data.length();

        JSONArray data1 = sunPaymentMoneyJsonObject.getJSONArray("data");

        for (int i = 0; i < length; i++) {
            JSONObject jsonObj = data.getJSONObject(i);
            JSONObject jsonObject1 = data1.getJSONObject(i);

            String coin = jsonObj.getString("coin");
            String order_no = jsonObj.getString("order_no");
            String tx_id = jsonObj.getString("tx_id");

            Integer pay_fee = (Integer) jsonObj.get("pay_fee");
            String created_at = jsonObj.getString("created_at");

            String paymenBatch = jsonObject1.getString("paymen_batch");
            BigDecimal user_payment_money = jsonObject1.getBigDecimal("sum(user_payment_money)");

            Timestamp ts = Timestamp.valueOf(created_at);  // 2011-05-09 11:49:45.0
            long time = ts.getTime()/1000L;
            String sql = "INSERT INTO `yp_pool_mq_order` " +
                    "(`req_id`, `coin`, `day`, `user_payment_money`, " +
                    "`user_payment_fee`, `tx_id`, `business_type`, `payment_status`, " +
                    "`start_pay`, `created_at`, `updated_at`, `remark`) VALUES ( " +
                    "'NORMAL_TYPE_"+paymenBatch+"_1', " +
                    "'"+coin+"', " +
                    time+", " +
                    user_payment_money.toPlainString()+", " +
                    (new BigDecimal(pay_fee)).divide(new BigDecimal("100000000")).toPlainString()+ ", " +
                    "'"+tx_id+"', " +
                    "'NORMAL_TYPE', 1, 1, '"+created_at+"', '"+created_at+"', '20240304补20230830-20231018的数据');" ;


            System.out.println(sql);

        }




    }
}
