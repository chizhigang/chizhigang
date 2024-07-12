package com.spiderpool.www.service;

public class ProducerTest {

    public void sendToKafkaTest(){
        String userName = "eli001" ;
//        String userName = "aaaaa" ;
        String workName = "10x" ;
        String allShareSavePath = "/data/app/share" ;

        int n = 0 ;
        long lastshareTime = System.currentTimeMillis() - n * 5 * 60 * 1000;
        System.out.println("lastshareTime "+lastshareTime);
        int currentTime = (int)(lastshareTime/ 1000);

        int shareTime = currentTime - currentTime % 300;

        TwoTuple<String, String> pathRes = HashRateLoadUtil.createSaveFileName(allShareSavePath, Coin.btc.getShortName(),
                shareTime);
        System.out.println(pathRes.second);
        for (int i = 0; i < 10; i++) {
            String share = String.format(share_format, userName, workName + i, lastshareTime + "");
            System.out.println("echo "+"\""+ share+"\" >> "+ pathRes.second);
        }
    }

}
