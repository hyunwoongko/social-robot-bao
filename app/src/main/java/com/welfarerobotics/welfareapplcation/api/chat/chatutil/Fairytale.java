package com.welfarerobotics.welfareapplcation.api.chat.chatutil;

import android.media.MediaPlayer;
import android.os.Environment;
import com.welfarerobotics.welfareapplcation.entity.Server;
import com.welfarerobotics.welfareapplcation.entity.ServerCache;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public final class Fairytale {
    private static Fairytale fairytale = null;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private boolean flag;

    private Fairytale() {

    }

    public synchronized static Fairytale get() {
        if (fairytale == null) fairytale = new Fairytale();
        return fairytale;
    }

    private Random randomint = new Random();
    private String[] titleArray = {"hungbuNolbu", "kingEarsDonkeyEars"};

    private String[] hungbuNolbu = {
            "흥부놀부전",
            "옛날 아주 먼 옛날, 착한 아우 와 욕심쟁이 형 이 한 마을에 살았습니다.",
            "아버지가 물려준 재산은 형인 놀부가 몽땅 차지해서 동생인 흥부는 가난하게 살아야 했습니다.",
            "아이가 많은 흥부는 아무리 열심히 일해도 살림이 나아지지 않았습니다.",
            "어느날 먹을 것이 떨어진 흥부는 할 수 없이 놀부를 찾아 갔습니다.",
            "형님, 쌀 좀 꾸어 주세요. 아이들이 굶고 있어요. 가을이 되면 꼭 갚겠습니다.",
            "뭐라고? 이 게으름뱅이 같은 놈! 열심히 일 할 생각은 않고 구걸이나 다니다니! 썩 나가거라!",
            "놀부는 담뱃대를 휘두르며 소리를 질러 흥부를 쫒아 버렸습니다.",
            "집으로 돌아 온 훙부는 아내와 함께 툇마루에 앉아 한숨을 쉬고 있었습니다.",
            "그런데 갑자기 시끄러운 소리가 들리면서 아기 제비 한 마리가 마당으로 툭 떨어졌습니다.",
            "제비 집을 올려다 보니, 커다란 구렁이 한 마리가 제비 둥지를 덮치려는 참이었습니다.",
            "흥부는 얼른 구렁이를 쫒아 버렸습니다.",
            "떨어진 아기 제비는 다리가 부러져 있었습니다.",
            "흥부는 고운 베와 실을 가져다 부러진 제비의 다리를 정성껏 묶어 주었습니다.",
            "다친 제비는 곧 나았고, 무럭무럭 자라나 훨훨 날아 다니게 되었습니다.",
            "그리고 가을이 되자 다른 제비들과 함께 남쪽으로 날아갔습니다.",
            "긴 겨울이 지나고 봄이 왔습니다.",
            "남쪽으로 갔던 제비들이 돌아 올 철입니다.",
            "흥부네 집에도 지난해 살았던 제비가 다시 돌아왔습니다.",
            "어이구, 너도 무사히 왔구나. 다리는 괜찮니?",
            "반기는 흥부에게 제비가 뭔가를 떨어뜨려 주었습니다.",
            "그것은 박 씨였습니다.",
            "흥부는 제비가 준 박 씨를 고이 심었습니다.",
            "곧 싹이 트고 덩굴이 뻗더니, 커다란 박이 주렁주렁 달렸습니다.",
            "허, 그 박 참 크기도 하다. 켜서 바가지 만들면 잘 팔리겠구나!",
            "흥부 내외는 흐뭇하게 박을 쳐다보며 말했습니다.",
            "드디어 박이 굳었습니다.",
            "박 속으로는 김치를 담그고, 껍데기로는 바가지를 만듭시다.",
            "흥부의 아내는 박을 하나 따다 놓고 켜기 시작했습니다.",
            "박이 쩍 갈라졌는데 이게 웬일입니까!",
            "안에서 금은 보화가 산더미같이 쏟아져 나오는 게 아니겠어요?",
            "흥부와 아내는 좋아서 어쩔 줄 모르다 또 하나를 켜기 시작했습니다.",
            "펑! 박이 갈라지더니 이번에는 호리병을 두 손에 든 선녀가 나왔습니다.",
            "선녀가 든 호리병은 원하는 것은 뭐든지 나오게 해 주는 요술 호리병이었습니다.",
            "흥부는 이제 대궐같은 집에서 사는 큰 부자가 되었습니다.",
            "이것을 알게 된 놀부는 흥부를 찾아가 호통을 쳤습니다.",
            "네 이놈! 너같은 게으름뱅이, 가난뱅이가 어떻게 이런 큰 부자가 됐느냐? 썩 사실대로 말하지 못할까?",
            "흥부는 놀부에게 제비가 물어다 준 박 씨 이야기를 자세히 해 주었습니다.",
            "급히 집으로 돌아논 놀부는 둥지 속의 아기 제비 한 마리를 꺼냈습니다.",
            "그리고 다리를 뚝 부러뜨리고는 두꺼운 실로 아무렇게나 칭칭 동여매 다시 둥지 속에 넣으면서 말했습니다.",
            "이 놈아, 내년 봄에 박 씨 하나를 꼭 물고 와야 한다. 어험!",
            "다음 해 봄, 제비들이 돌아왔습니다, 그리고 안절부절못해 기다리던 놀부 부부 앞에 박 씨 하나를 떨어뜨려 주었습니다.",
            "거 기왕이면 두 개쯤 주지 그러는구나.",
            "놀부는 반가운 가운데도 이렇게 욕심을 부렸습니다.",
            "뒤곁에 심은 박 씨는 금세 잎을 내고 덩굴을 뻗어 올렸습니다, 물동이 같은 박이 주렁주렁 열렸습니다.",
            "저 박을 켜면 금은보화가 쏟아져 나오겠지요, 영감?",
            "암, 그렇고 말고. 흥부네보다 더 큰 부자가 될걸, 어허허.",
            "놀부와 아내는 박이 굳기만 기다렸습니다.",
            "가을이 왔습니다. 이제 켜도 되겠지. 가장 큰 놈으로 골라서... 어험! 놀부는 아내와 함께 박을 켜기 시작했습니다.",
            "슬금슬금 톱질하세",
            "금 나와라 톱질하세",
            "은 나와라 톱질하세",
            "박이 쩍 갈라졌습니다.",
            "그런데 이게 웬일입니까?",
            "금은보화는 안 나오고 무당이 칼을 휘두르고 춤을 추면서 나타난 것입니다.",
            "에이, 이 고얀 놈아. 욕심쟁이 놀부야. 천벌을 받아 마땅하거늘, 천벌을 면하려거든 돈과 패물을 몽땅 신령님께 바쳐야 하느니라, 어험!",
            "놀부와 부인은 벌벌 떨며 돈과 패물을 모두 가져다 바쳤습니다.",
            "이 박에서는 금은보화가 나오겠지.",
            "놀부는 박 하나를 또 켰습니다. 박이 쩍 갈라지더니 이번에는 도깨비가 나타났습니다.",
            "도깨비는 육모 방망이로 놀부와 아내를 사정없이 때려 주었습니다.",
            "집도 돈도 패물도 모두 뺏기고 실컷 두들겨 맞아 앓고 있는 놀부 내외를 흥부가 데리러 왔습니다.",
            "형님, 저희가 도와 드릴테니 걱정마세요.",
            "그 뒤 놀부는 잘못을 깨닫고 흥부와 사이좋게 지냈습니다."};

    private String[] kingEarsDonkeyEars = {
            "임금님 귀는 당나귀 귀",
            "옛날에 아주 큰 귀를 가진 임금님이 있었어요.",
            "임금님은 당나귀 귀처럼 생긴 커다란 귀가 창피했어요.",
            "그래서 언제나 커다란 왕관을 써서 가렸답니다.",
            "근사한 왕관 아래 별난 귀가 숨어 있다는 사실은 아무도 모르는 비밀이었어요.",
            "단 한 사람, 왕관을 만든 일꾼만 빼고요.",
            "훌륭한 임금님일수록 큰 왕관을 쓰는거야.",
            "맞아, 맞아! 우리 임금님은 정말 훌륭해",
            "백성들은 모두 이렇게 믿었답니다.",
            "큭큭! 당나귀 같은 귀를 가리려는 건 줄도 모르고!",
            "일꾼은 혼자서 웃을 뿐입니다.",
            "다른 사람에게 말해서는 안되니까요.",
            "하지만 이런 재밌는 얘기를 아무에게도 못 하다니!",
            "참고 있기가 여간 힘든 일이 아니었어요.",
            "얘기가 하고 싶어서 입이 간질간질할 정도였지요.",
            "그러던 어느 날, 좋은 수가 떠올랐습니다.",
            "그렇지! 그렇게 하면 되겠군!",
            "일꾼은 집 뒤에 있는 산으로 올라갔어요.",
            "뒷산에는 빽빽하게 자란 대나무 숲이 있었어요.",
            "이곳에 다다르자 두 손을 모아 입에 대고는 큰 소리로 외칩니다.",
            "임금님 귀는 당나귀 귀!~~",
            "임금님 귀는 당나귀 귀!~~",
            "일꾼은 목청껏 외쳤어요.",
            "그동안 참고 참느라 답답했던 마음이 후련하게 풀리는 듯 했습니다.",
            "그런데 이상한 일이 벌어졌어요.",
            "일꾼의 목소리가 메아리가 되어 숲을 맴도는 거예요.",
            "임금님 귀는 당나귀 귀!~~",
            "임금님 귀는 당나귀 귀!~~",
            "메아리는 산을 타고 내려와 마을을 떠돌아 다닙니다.",
            "임금님 귀는 당나귀 귀!~~",
            "임금님 귀는 당나귀 귀!~~",
            "사람들의 귀에 똑똑히 들렸어요.",
            "메아리는 소문을 타고 온 나라에 퍼졌습니다.",
            "임금님이 머리 끝까지 화가 나는 건 당연한 일이죠.",
            "임금님은 그 대나무들을 몽땅 베어 버려라! 라고 말했어요.",
            "신하들은 대나무를 다 베었지만 메아리는 멈추지 않았어요.",
            "임금님 귀는 당나귀 귀!~~",
            "임금님 귀는 당나귀 귀!~~",
            "이제 어떻게 해야 할까요?",
            "이제 내 귀가 크다는 것을 온 백성이 다 알게 됐는데 애써 귀를 가릴 필요가 없어졌구나!",
            "임금님은 왕관을 벗어 놓고 백성들 앞에 섰습니다.",
            "이제는 내 귀를 드러내 놓겠다!",
            "큰 귀로 백성들의 이야기를 더욱 귀담아 듣겠노라!",
            "임금님이 말하자 백성들 모두 크게 기뻐했답니다."
    };

    private void playVoice(MediaPlayer mediaPlayer, String tts) {
        try {

            String text = URLEncoder.encode(tts, "UTF-8"); // 13자
            String apiURL = "https://naveropenapi.apigw.ntruss.com/voice/v1/tts";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", ServerCache.getInstance().getCssid());
            con.setRequestProperty("X-NCP-APIGW-API-KEY", ServerCache.getInstance().getCsssecret());
            // post request
            String postParams = "speaker=jinho&speed=2.5&text=" + text;
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) { // 정상 호출
                InputStream is = con.getInputStream();
                int read = 0;
                byte[] bytes = new byte[1024];
                //NaverCSS 폴더 생성
                File dir = new File(Environment.getExternalStorageDirectory() + "/", "NaverCSS");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                // 랜덤한 이름으로 mp3 파일 생성
                String tempname = "navercssfile";
                File f = new File(Environment.getExternalStorageDirectory() +
                        File.separator + "NaverCSS/" + tempname + ".mp3");
                f.createNewFile();
                OutputStream outputStream = new FileOutputStream(f);
                while ((read = is.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
                is.close();

            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();
                System.out.println(response.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String tempname = "navercssfile";
        String Path_to_file = Environment.getExternalStorageDirectory() +
                File.separator + "NaverCSS/" + tempname + ".mp3";

        try {
            mediaPlayer.setDataSource(Path_to_file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
    }

    public void play() {
        flag = true;
        String selector = titleArray[randomint.nextInt(titleArray.length)];
        if (titleArray[0].equals(selector)) {
            mediaPlayer = new MediaPlayer();
            playVoice(mediaPlayer, hungbuNolbu[0]);
            int idx = 0;
            while (flag) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                        mediaPlayer.release();
                        mediaPlayer = new MediaPlayer();
                        idx++;
                        playVoice(mediaPlayer, hungbuNolbu[idx]);
                    }
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
        }
        else if (titleArray[1].equals(selector)) {
            mediaPlayer = new MediaPlayer();
            playVoice(mediaPlayer, kingEarsDonkeyEars[0]);
            int idx = 0;
            while (flag) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                        mediaPlayer.release();
                        mediaPlayer = new MediaPlayer();
                        idx++;
                        playVoice(mediaPlayer, kingEarsDonkeyEars[idx]);
                    }
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void stop() {
        mediaPlayer.stop();
        mediaPlayer.release();
        flag = false;
    }
}
