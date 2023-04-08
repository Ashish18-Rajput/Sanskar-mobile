//package com.sanskar.tv.EPG;
//
//
//
//
//
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Random;
//
//
//public class MockDataService {
//
//    private static Random rand = new Random();
//    private static List<Integer> availableEventLength = Lists.newArrayList(
//            1000*60*15,  // 15 minutes
//            1000*60*30,  // 30 minutes
//            1000*60*45,  // 45 minutes
//            1000*60*60,  // 60 minutes
//            1000*60*120  // 120 minutes
//    );
//
//    /*private static List<String> availableEventTitles = Lists.newArrayList(
//            "Avengers",
//            "How I Met Your Mother",
//            "Silicon Valley",
//            "Late Night with Jimmy Fallon",
//            "The Big Bang Theory",
//            "Leon",
//            "Die Hard"
//    );*/
//
//    private static List<String> availableChannelLogos = Lists.newArrayList(
//            "https://bhaktiappproduction.s3.ap-south-1.amazonaws.com/videos/video/1566764sanskar.png",
//            "https://bhaktiappproduction.s3.ap-south-1.amazonaws.com/videos/video/2520556satsang.png",
//            "https://bhaktiappproduction.s3.ap-south-1.amazonaws.com/videos/video/5814954shubh.png",
//            "https://bhaktiappproduction.s3.ap-south-1.amazonaws.com/videos/video/1676579235326sanskar_uk.png",
//            "https://bhaktiappproduction.s3.ap-south-1.amazonaws.com/videos/video/74195374240428sanskar_usa.png"
//    );
//
//    public static Map<EPGChannel, List<EPGEvent>> getMockData() {
//        HashMap<EPGChannel, List<EPGEvent>> result = Maps.newLinkedHashMap();
//
//        long nowMillis = System.currentTimeMillis();
//
//        for (int i=0 ; i < 20 ; i++) {
//            EPGChannel epgChannel = new EPGChannel(availableChannelLogos.get(i % 5),
//                    "Channel " + (i+1), Integer.toString(i),"");
//
//            result.put(epgChannel, createEvents(epgChannel, nowMillis));
//        }
//
//        return result;
//    }
//
//    private static List<EPGEvent> createEvents(EPGChannel epgChannel, long nowMillis) {
//        List<EPGEvent> result = Lists.newArrayList();
//
//        long epgStart = nowMillis - EPG.DAYS_BACK_MILLIS;
//        long epgEnd = nowMillis + EPG.DAYS_FORWARD_MILLIS;
//
//        long currentTime = epgStart;
//
//        while (currentTime <= epgEnd) {
//            long eventEnd = getEventEnd(currentTime);
//            EPGEvent epgEvent = new EPGEvent(currentTime, eventEnd,"","", availableEventTitles.get(randomBetween(0, 6)),"","");
//            result.add(epgEvent);
//            currentTime = eventEnd;
//        }
//
//        return result;
//    }
//
//    private static long getEventEnd(long eventStartMillis) {
//        long length = availableEventLength.get(randomBetween(0,4));
//        return eventStartMillis + length;
//    }
//
//    private static int randomBetween(int start, int end) {
//        return start + rand.nextInt((end - start) + 1);
//    }
//}
