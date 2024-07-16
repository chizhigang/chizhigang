package com.spiderpool.www.service;

import com.spiderpool.www.ChizgSpringbootApplication;
import com.ypt.pool.common.constant.Coin;
import com.ypt.pool.common.dto.TwoTuple;
import com.ypt.pool.common.util.HashRateLoadUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChizgSpringbootApplication.class)
@Slf4j
public class ProducerTest {

    private static final String share_format = "524288:312500000:79495195323031.48437500000000000000:%s.%s:%s";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    public void sendToKafkaTest01() {
//        String userName = "eli001" ;
        String userName = "aaaaa";
        String workName = "10x";
        String allShareSavePath = "/data/app/share";

        int n = 0;
        long lastshareTime = System.currentTimeMillis() - n * 5 * 60 * 1000;
        System.out.println("lastshareTime " + lastshareTime);

        int i = 1;
        String share = String.format(share_format, userName, workName + i, lastshareTime + "");
        kafkaTemplate.send("dev_BtcShare", share);
    }

    @Test
    public void sendToKafkaTest() {
        String userName = "eli001";
//        String userName = "aaaaa" ;
        String workName = "10x";
        String allShareSavePath = "/data/app/share";

        int n = 0;
        long lastshareTime = System.currentTimeMillis() - n * 5 * 60 * 1000;
        System.out.println("lastshareTime " + lastshareTime);
        int currentTime = (int) (lastshareTime / 1000);

        int shareTime = currentTime - currentTime % 300;

        TwoTuple<String, String> pathRes = HashRateLoadUtil.createSaveFileName(allShareSavePath, Coin.btc.getShortName(),
                shareTime);
        System.out.println(pathRes.second);
        for (int i = 0; i < 10; i++) {
            String share = String.format(share_format, userName, workName + i, lastshareTime + "");
//            System.out.println("echo "+"\""+ share+"\" >> "+ pathRes.second);
        }
    }

}
