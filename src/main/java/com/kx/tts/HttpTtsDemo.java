package com.kx.tts;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.FileReader;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class HttpTtsDemo {
	private static  Logger logger = LoggerFactory.getLogger(HttpTtsDemo.class);
    private String url = "http://nlsapi.aliyun.com/speak?";
    private static String tts_text = "挂单成功";
    public static void main(String[] args) throws IOException {
        //请使用https://ak-console.aliyun.com/ 页面获取的Access 信息
        //请提前开通智能语音服务(https://data.aliyun.com/product/nls)
        String ak_id = "LTAIlQCaIk2vriyQ";
        String ak_secret = "vnQwVGLf1OrPbUyg0UNNvBn2WNwiBq";
        //设置TTS的参数,详细参数说明详见文档部分2.1 参数配置
        HttpTtsDemo ttsDemo=new HttpTtsDemo();
        TtsRequest ttsRequest = new TtsRequest();
        ttsRequest.setEncodeType("wav");
        ttsRequest.setVoiceName("xiaoyun");
        ttsRequest.setVolume(100);
        ttsRequest.setSampleRate(16000);
        ttsRequest.setSpeechRate(0);
        ttsRequest.setPitchRate(-100);
        ttsRequest.setTtsNus(1);
        ttsRequest.setBackgroundMusicId(0);
        ttsRequest.setBackgroundMusicOffset(0);
        ttsRequest.setBackgroundMusicVolume(100);
        String url = ttsDemo.url+"encode_type="+ttsRequest.getEncodeType()
                 +"&voice_name="+ttsRequest.getVoiceName()
                 +"&volume="+ttsRequest.getVolume()
                 +"&sample_rate="+ttsRequest.getSampleRate()
                 +"&speech_rate="+ttsRequest.getSpeechRate()
                 +"&pitch_rate="+ttsRequest.getPitchRate()
                 +"&tts_nus="+ttsRequest.getTtsNus()
                +"&background_music_id="+ttsRequest.getBackgroundMusicId()
                +"&background_music_offset="+ttsRequest.getBackgroundMusicOffset()
                +"&background_music_volume="+ttsRequest.getBackgroundMusicVolume();
        logger.info("TTS request is: {}",url);
        String fileName = UUID.randomUUID().toString().replace("-","");
        //tts demo 会在项目根目录生产语音文件
        HttpResponse response = HttpUtil.sendTtsPost(tts_text,ttsRequest.getEncodeType(), fileName ,url, ak_id, ak_secret);
    }
}
