package comyhyhyhte.example.lx.calculator;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static android.R.attr.x;
import static comyhyhyhte.example.lx.calculator.R.id.screen;
import static comyhyhyhte.example.lx.calculator.R.id.textView;


public class MainActivity extends AppCompatActivity {
    private Button[] btnNum=new Button[10];//数字按钮
    private Button[] btnSymbol=new Button[4];//符号按钮
    private TextView textView;//显示内容
    private Button Clean;//清除按钮
    private Button Delete;//删除按钮
    private Button Point;//小数点
    private Button equal;//等于号
    private Button right;//右括号
    private Button left;//左括号
    private double result;//计算结果
    private boolean firstFlag;//是否是第一次输入
    private boolean cleanFlag;//是否清空内容框
    private String lastSymbol;//用来保存运算符
    private String hasStr;//显示框中当前的内容
    private int countPonit=0;//输入小数点时用来统计点击次数的计数器
    private int countEquals=0;//判断等号输入的次数
    private int countZiFu=0;//用户输入的字符总长度
    private int countYunSuanFu=0;//计算中遇到的运算符的次数
    /**
     * 通过构造函数对数据进行初始化
     */
    public MainActivity(){
        firstFlag=true;//是第一次输入
        cleanFlag=false;//不需要清空屏幕
        result=0;//在未进行计算前，结果为0
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();//隐藏标题栏
        btnNum[0]=(Button)this.findViewById(R.id.button0);
        btnNum[1]=(Button)this.findViewById(R.id.button1);
        btnNum[2]=(Button)this.findViewById(R.id.button2);
        btnNum[3]=(Button)this.findViewById(R.id.button3);
        btnNum[4]=(Button)this.findViewById(R.id.button4);
        btnNum[5]=(Button)this.findViewById(R.id.button5);
        btnNum[6]=(Button)this.findViewById(R.id.button6);
        btnNum[7]=(Button)this.findViewById(R.id.button7);
        btnNum[8]=(Button)this.findViewById(R.id.button8);
        btnNum[9]=(Button)this.findViewById(R.id.button9);
        btnSymbol[0]=(Button)this.findViewById(R.id.buttonAdd);
        btnSymbol[1]=(Button)this.findViewById(R.id.buttonReduce);
        btnSymbol[2]=(Button)this.findViewById(R.id.buttonMul);
        btnSymbol[3]=(Button)this.findViewById(R.id.buttonExc);
        equal=(Button)this.findViewById(R.id.equal);
        Clean=(Button)this.findViewById(R.id.buttonClean);
        Delete=(Button)this.findViewById(R.id.buttonDelete);
        Point=(Button)this.findViewById(R.id.buttonPoint);
        right=(Button)this.findViewById(R.id.right);
        left=(Button)this.findViewById(R.id.left);
        textView=(TextView) this.findViewById(R.id.textView);
        textView.setText("0");//将显示框上的内容初始化为0
        textView.setInputType(InputType.TYPE_NULL);//禁止手机软键盘
        String input1=load();
        if(!TextUtils.isEmpty(input1)){
            textView.setText(input1);
        }
        /**
         * 使用foreach()方法来遍历每一个数字按钮和符号按钮
         */
        ButtonNumListener bnl=new ButtonNumListener();
        for (Button na:btnNum){
            na.setOnClickListener(bnl);
        }
        ButtonSymbolListener bsl=new ButtonSymbolListener();
        for (Button sa:btnSymbol){
            sa.setOnClickListener(bsl);
        }
        ButtonEqualListener bel =new ButtonEqualListener();//为等号注册监听
        equal.setOnClickListener(bel);
        ButtonOtherListener bol=new ButtonOtherListener();
        Clean.setOnClickListener(bol);
        Delete.setOnClickListener(bol);
        Point.setOnClickListener(bol);
        right.setOnClickListener(bol);
        left.setOnClickListener(bol);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        String inputText=textView.getText().toString();
        save(inputText);
    }

    /**
     * 等号，数字，及其它运算符号的监听事件
     */

    //数字按钮的监听事件
    private class ButtonNumListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            countZiFu++;
            if(countZiFu>11){
                Toast.makeText(MainActivity.this, "最多只能输入11个字符!", Toast.LENGTH_SHORT).show();
                countZiFu=11;
            }else {
                Button btn1 = (Button) v;
                String input1 = btn1.getText() + "";
                hasStr = textView.getText() + "";
                if (firstFlag) {
                    textView.setText(input1);
                    firstFlag = false;
                } else if (hasStr.equals("0")) {
                    textView.setText(input1);
                } else {
                    hasStr = textView.getText() + "";
                    hasStr = hasStr + input1;
                    textView.setText(hasStr);
                }
            }
        }
    }
    //加减乘除的监听事件
    private class ButtonSymbolListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            countYunSuanFu++;
            countZiFu++;
            if(countZiFu>11){
                Toast.makeText(MainActivity.this, "最多只能输入11个字符!", Toast.LENGTH_SHORT).show();
                countZiFu=11;
            }else {
                Button btn2 = (Button) v;
                String input2 = btn2.getText() + "";
                hasStr = textView.getText() + "";
                if (firstFlag || hasStr.substring(hasStr.length() - 1, hasStr.length()).equals("+")
                        || hasStr.substring(hasStr.length() - 1, hasStr.length()).equals("-")
                        || hasStr.substring(hasStr.length() - 1, hasStr.length()).equals("×")
                        || hasStr.substring(hasStr.length() - 1, hasStr.length()).equals("÷")
                        || hasStr.substring(hasStr.length() - 1, hasStr.length()).equals("=")) {
                    textView.setText(hasStr);
                } else {
                    hasStr = hasStr + input2;
                    textView.setText(hasStr);
                    countPonit = 0;
                }
            }
            countPonit=0;
        }
    }

    //等于号的监听事件
    private class ButtonEqualListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Button btn=(Button)v;
            hasStr=textView.getText()+"";
            hasStr=hasStr+"#";
            //hasStr=hasStr.trim();
            char[] equatio=hasStr.toCharArray();//将当前的运算表达式转化为字符数组
            char[] 	calculate=new char[100];
            change(equatio,calculate);
            result=getResult(calculate);
            hasStr=textView.getText()+"";
            hasStr=hasStr.trim();
            String hasStrLast=hasStr.substring(hasStr.length()-1,hasStr.length());
            if(hasStr.equals("错误")){
                countYunSuanFu = 0;
                countPonit = 0;
            }else if(hasStrLast.equals("+")||hasStrLast.equals("-")||hasStrLast.equals("×")||hasStrLast.equals("÷")||hasStrLast.equals(".")||hasStrLast.equals("(")||hasStrLast.equals(")")){
                textView.setText("错误");
                countYunSuanFu = 0;
                countPonit = 0;
            }else {
                String resultXianShi = result + "";
                if (resultXianShi.length() > 11) {
                    resultXianShi = resultXianShi.substring(0, 10);
                }
                textView.setText(resultXianShi);
                countZiFu = resultXianShi.length();
                countYunSuanFu = 0;
                countPonit = 0;
            }
        }
    }

    //其他符号的监听的事件
    private class ButtonOtherListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.right:
                    countZiFu++;
                    if(countZiFu>11){
                        Toast.makeText(MainActivity.this, "最多只能输入11个字符!", Toast.LENGTH_SHORT).show();
                        countZiFu=11;
                    }else{
                        String barcketRight_input = right.getText() + "";
                        hasStr = textView.getText() + "";
                        if (hasStr.length() == 1) {
                            if (hasStr.equals("(")) {
                                hasStr = hasStr + barcketRight_input;
                            }
                            textView.setText(hasStr);
                        } else {
                            hasStr = hasStr + barcketRight_input;
                            textView.setText(hasStr);
                        }
                    }
                    break;
                case R.id.left:
                    countZiFu++;
                    if(countZiFu>11){
                        Toast.makeText(MainActivity.this, "最多只能输入11个字符!", Toast.LENGTH_SHORT).show();
                        countZiFu=11;
                    }else {
                        String barcketLeft_input = left.getText() + "";
                        hasStr = textView.getText() + "";
                        if (hasStr.length() == 1 && hasStr.equals("0")) {
                            hasStr = barcketLeft_input;
                        } else {
                            hasStr = hasStr + barcketLeft_input;
                        }
                        textView.setText(hasStr);
                    }
                    break;
                case R.id.buttonPoint:
                    if(countPonit==0) {
                        countZiFu++;
                        if (countZiFu > 11) {
                            Toast.makeText(MainActivity.this, "最多只能输入11个字符!", Toast.LENGTH_SHORT).show();
                            countZiFu = 11;
                        } else {
                            String point_input = Point.getText() + "";
                            hasStr = textView.getText() + "";
                            if (firstFlag || hasStr.substring(hasStr.length() - 1, hasStr.length()).equals("+")
                                    || hasStr.substring(hasStr.length() - 1, hasStr.length()).equals("-")
                                    || hasStr.substring(hasStr.length() - 1, hasStr.length()).equals("×")
                                    || hasStr.substring(hasStr.length() - 1, hasStr.length()).equals("÷")
                                    || hasStr.substring(hasStr.length() - 1, hasStr.length()).equals("=")) {
                                textView.setText(hasStr);
                            } else if (countPonit == 0) {
                                hasStr = hasStr + point_input;
                            }
                            textView.setText(hasStr);
                        }
                    }
                    break;
                case R.id.buttonClean:
                    textView.setText("0");
                    firstFlag = true;
                    result = 0;
                    countPonit = 0;
                    countEquals = 0;
                    cleanFlag = false;
                    countZiFu = 0;
                    break;
                case R.id.buttonDelete:
                    hasStr = textView.getText() + "";//内容框中所有存在的字符
                    String lastSymbol = hasStr.substring(hasStr.length() - 1, hasStr.length());
                    if (hasStr.length() == 1) {
                        hasStr = "0";
                        result = 0;
                        countPonit = 0;
                        firstFlag = true;
                        cleanFlag = false;
                        countEquals = 0;
                        countZiFu = 0;
                    } else if (lastSymbol.equals(".")) {
                        hasStr = hasStr.substring(0, hasStr.length() - 1);
                        countPonit = 0;
                        countZiFu--;
                    } else {
                        hasStr = hasStr.substring(0, hasStr.length() - 1);//substring()是用来截取字符串的函数
                        countZiFu--;
                    }
                    textView.setText(hasStr);//将改变后的字符显示在显示框中
                    break;
                default:
            }
        }
    }
    public class DeShu{
        private int a;
    }
    DeShu deShu=new DeShu();

    //将字符串(字符串中的内容为数字)转化为数字
    public double readNumber(char equatio[],int i){
            double x=0;
            int k=0;//i是用来遍历整个数组的参数，k
            //先处理整数
            while(equatio[i]>='0'&&equatio[i]<='9'){
                x=x*10+(equatio[i]-'0');
                i++;
            }
            //再处理小数
            if(equatio[i]=='.'){
                i++;
                while(equatio[i]>='0'&&equatio[i]<='9'){
                    x=x*10+(equatio[i]-'0');
                    i++;
                    k++;
                }
            }
            while(k!=0){
                x=x/10;
                k=k-1;
            }
        deShu.a=i;
        return x;
    }
    //判断是否为操作符
    public int is_operation(char op){
        switch (op){
            case '+':
            case '-':
            case '×':
            case '÷':
                return 1;
            default:
                return 0;
        }
    }
    //判断操作符的优先级
    public int priority(char op){
        switch(op){
            case '#':
                return -1;
            case '(':
                return 0;
            case '+':
            case '-':
                return 1;
            case '×':
            case '÷':
                return 2;
            default:
                return -1;
        }
    }
    /**
     * 将中缀表达式转化为后缀表达式
     * e和f分别用于存储中缀表达式和后缀表达式
     * @param e
     * @param f
     */
    public void change(char e[],char f[]){
        int i=0,j=0;
        char[] opst=new char[100];//定义了一个操作栈
        int top=0;//栈顶指针
        int t;
        opst[top]='#';
        top++;
        while(e[i]!='#'){
            if(e[i]>='0'&&e[i]<='9'||e[i]=='.'){
                f[j++]=e[i];//遇到数字和小数点直接写入后缀表达式
            }else if(e[i]=='('){
                opst[top]=e[i];//遇到左括号写入操作符栈
                top++;
            }else if(e[i]==')'){
                //遇到')'将与之对应的'('后的操作符全部写入后缀表达式中
                t=top-1;
                while (opst[t]!='('){
                    f[j++]=opst[--top];
                    t=top-1;
                }
                top--;//'('出栈
            }else if(is_operation(e[i])==1){
                f[j++]=' ';//用空格分开两个操作数
                while(priority(opst[top-1])>=priority(e[i])){
                    f[j++]=opst[--top];
                }
                opst[top]=e[i];//当前元素进栈
                top++;
            }
            i++;//处理下一个元素
        }
        while(top!=0){
            f[j++]=opst[--top];
        }
    }

    //后缀表达式求值
    public double getResult(char f[]) {
        double[] obst=new double[100];//操作数栈
        int top=0;//栈顶指针
        int i=0;
        double x1,x2;
        while(f[i]!='#'){
            if(f[i]>='0'&&f[i]<='9'||f[i]=='.'){
                obst[top] = readNumber(f, i);
                top++;
                i=deShu.a;
            }else if(f[i]==' '){
                i++;//两个操作数之间用一个空格隔开
            }else if(f[i]=='+'){
                countYunSuanFu++;
                x2=obst[--top];
                x1=obst[--top];
                obst[top]=x1+x2;
                top++;
                i++;
            }else if(f[i]=='-'){
                countYunSuanFu++;
                x2=obst[--top];
                x1=obst[--top];
                obst[top]=x1-x2;
                top++;
                i++;
            }else if(f[i]=='×'){
                countYunSuanFu++;
                x2=obst[--top];
                x1=obst[--top];
                obst[top]=x1*x2;
                top++;
                i++;
            }else if(f[i]=='÷'){
                countYunSuanFu++;
                x2=obst[--top];
                x1=obst[--top];
                if(x2==0){
                    hasStr="错误";
                    textView.setText(hasStr);
                    break;
                }else{
                    obst[top]=x1/x2;
                    top++;
                    i++;
                }
            }
        }
        return obst[0];
    }
    public void save(String inputText){
        FileOutputStream out=null;
        BufferedWriter writer=null;
        try{
            out=openFileOutput("data", Context.MODE_PRIVATE);
            writer=new BufferedWriter(new OutputStreamWriter(out));
            writer.write(inputText);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    public String load(){
        FileInputStream in=null;
        BufferedReader reader=null;
        StringBuilder context=new StringBuilder();
        try{
            in=openFileInput("data");
            reader=new BufferedReader(new InputStreamReader(in));
            String line="";
            while((line=reader.readLine())!=null){
                context.append(line);
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally {
            if(reader!=null){
                try {
                    reader.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
        return context.toString();
    }
}
