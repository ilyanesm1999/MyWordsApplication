package soft.example.com.mywordsapplication;


import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/**
 * Created by admin on 27.12.2015.
 */
public class MyGame {
    HashMap<String, Integer> words = new HashMap<String, Integer>();
    int width,height;
    public MyView[][] views;
    int currentWordColor = getRandomColor();
    public int score;
    MyDBHelper dbHelper;
    public ArrayList<Point> wordPath = new ArrayList<Point>();
    public HashMap<Integer,Boolean> colrs = new HashMap<>();

    public int getRandomColor(){
        Random rand = new Random();
        int r = rand.nextInt(255);
        int g = rand.nextInt(255);
        int b = rand.nextInt(255);
        int randomColor = Color.rgb(r, g, b);
        return randomColor;

    }
/*
    public void initMycolrs(){
        Log.i("mytag2", "initcolor ");
                 //colrs.put(Color.BLACK,false);
        if(colrs == null){
            Log.i("mytag2", "initcolor ogogo ");
            colrs = new HashMap<Integer,Boolean>();
            Log.i("mytag2", "initcolor 999");
        }
        colrs.put(Color.GREEN, false);
        colrs.put(Color.RED, false);
        colrs.put(Color.MAGENTA,false);

        colrs.put(Color.YELLOW,false);
        //colrs.put(Color.BLUE,false);
        colrs.put(Color.rgb(194,83,45),false);
        colrs.put(Color.rgb(56,223,232),false);
        colrs.put(Color.rgb(222,150,27),false);
        colrs.put(Color.rgb(121,133,212),false);
        colrs.put(Color.rgb(153,207,151),false);
        colrs.put(Color.rgb(207, 177, 151), false);

    }
*/
    public MyGame(MyDBHelper dbh,int h, int w) {
        dbHelper = dbh;
        views = new MyView[h][w];
        words = dbHelper.getAllWords();
        score = Integer.parseInt(dbHelper.getValueByKey("score"));
        width = w;
        height = h;

       // initMycolrs();
    }

    public String getRandomLetter(){
        Random generator = new Random();
        String letters = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
        return String.valueOf(letters.charAt(generator.nextInt(letters.length())));
    }

    public String getRandomWord(int maxLen){
        boolean f = true;
        String s = "";
        Random generator = new Random();
        Object[] keys = words.keySet().toArray();

        while(f) {
            s = (String) keys[generator.nextInt(words.size() - 1)];
            if (s.length() <= maxLen) {
                f = false;
            }
        }
        Log.i("mytag", "S = "+s);
        return s;

    }

    public boolean isWord(String s){
        return words.containsKey(s);
    }

    public boolean isFoundedWord(String word){
        boolean result = false;
        Log.i("mytag2", "1");
        HashMap<String, Object> map = dbHelper.getWordRow(word);
        Log.i("mytag2", "1.5");
        //Log.i("mytag", " map " + word + "founded" + (map.get("founded")).toString());
        if((int)map.get("founded") == 1){
            Log.i("mytag", " true result isfounded ");
            result = true;
        }
        Log.i("mytag2", "2");
        return result;
    }

    public void setWordFounded(String word){
        dbHelper.setAnybody("sg_entry","UNAME",word,"founded","1");
    }



    public void fillWords(int i, int j, String word, int position){

        if(position >= word.length()){
            // определить максимальн возм длину слова ?????????? дыркоопределитель
            int maxLen = 55555;
            currentWordColor = getRandomColor();
            String ss = getRandomWord(maxLen);
            wordPath = new ArrayList<Point>();
            Log.i("mytag", " вызываем new fillWords("+i+","+j+","+ss+",0) ");
            fillWords(i,j,ss,0);
            return;
        }

        Random generator = new Random();

    String logs = "Пихаем букву "+Character.toString(word.charAt(position))+ " в позицию "+i+" "+j+" ";
        Point ppp = new Point(i,j);
        wordPath.add(ppp);

        views[i][j].setLetter(Character.toString(word.charAt(position)), Color.BLACK);
        // предусмотреть запоминание пути
        views[i][j].filled = true;
        views[i][j].looseColor = currentWordColor;
        position++;

        // определяем есть ли соседняя пустая
        boolean goodGoExist = false;
        if((i != 0) && !views[i-1][j].filled)goodGoExist = true;
        if((j != width-1) && !views[i][j+1].filled)goodGoExist = true;
        if((i != height-1) && !views[i+1][j].filled)goodGoExist = true;
        if((j != 0) && !views[i][j-1].filled)goodGoExist = true;
        logs += " goodGoExist = "+goodGoExist;
        if(goodGoExist) {
//            наугад выбираем соседнюю пустую
            boolean goodGo = false;
            while (!goodGo) {
                int rp = generator.nextInt(4);
                String pp="";
                switch (rp) {
                    case 0:
                        i--;pp="up "+i+" "+j+" ";
                        break; // up
                    case 1:
                        j++;pp="right "+i+" "+j+" ";
                        break; // right
                    case 2:
                        i++;pp="down "+i+" "+j+" ";
                        break; // down
                    case 3:
                        j--;pp="left "+i+" "+j+" ";
                        break; // left
                }
                if ((i < height) && (i >= 0) && (j < width) && (j >= 0) && (!views[i][j].filled)) {
                    goodGo = true;//
                    logs += " путь в "+pp+" - "+goodGo;
                }else{
                    //откат раз попали в непустую клетку
                    switch (rp) {
                        case 0:
                            i++;
                            break; // up
                        case 1:
                            j--;
                            break; // right
                        case 2:
                            i--;
                            break; // down
                        case 3:
                            j++;
                            break; // left
                    }
                }
            }
        }else{
            if(position < word.length()){
                // порвали слово -> откат
                for(Point p:wordPath){
                    views[p.x][p.y].setText("");
                    views[p.x][p.y].filled = false;
                }

                if(wordPath.size() >= 3) {
                    // пихаем новое слово ровно той длины как влезшая часть пихавшегося невлезшего слова туда же
                    word = getRandomWord(wordPath.size()-1);
                    wordPath = new ArrayList<Point>();
                    position = 0;
                    for (Point p : wordPath) {
                        views[p.x][p.y].setText(word.charAt(position));
                        position++;
                        views[p.x][p.y].filled = true;
                    }
                }else{
                    // если же влезло 1-2 буквы то считаем это незаполняемой дырой и пихаем случайные буквы в нее
                    for (Point p : wordPath) {
                        String rndLetter = getRandomLetter();
                        views[p.x][p.y].setText(rndLetter);
                        views[p.x][p.y].setLetter(rndLetter, Color.BLACK);
                        views[p.x][p.y].looseColor = Color.BLUE;
                        views[p.x][p.y].filled = true;
                    }
                    wordPath = new ArrayList<Point>();
                }


            }
            // ищем первую пустую клетку
            for(int ii=0;ii < height;ii++) {
                for(int jj=0;jj < width;jj++) {
                    if(!views[ii][jj].filled){
                        word = getRandomWord(7777777);
                        wordPath = new ArrayList<Point>();
                        position = 0;
                        goodGoExist = true;
                        i = ii;
                        j = jj;
                        logs += " нашли пустую "+i+" "+j+" ";
                    }
                }
            }
        }

        if(!goodGoExist){
            // совсем некуда пихать
            return;
        }




            Log.i("mytag", logs+" вызываем fillWords("+i+","+j+","+word+","+position+") ");
                    fillWords(i, j, word, position);




    }


}
