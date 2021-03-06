package com.kisaann.thedining;

import android.content.DialogInterface;
import android.os.CountDownTimer;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kisaann.thedining.Common.Common;
import com.kisaann.thedining.Models.OffersCouponsModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.paperdb.Paper;

public class GameActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView img_back;
    private int[][] sudoku;
    private Button currentBtn;
    SudokuGenerator sg;
    private int spacesCount = 15;
    private boolean isSolved = false;
    ProgressBar progressBar;
    MyCountDownTimer myCountDownTimer;
    TextView txt_countDown;
    int i ;
    OffersCouponsModel offersCouponsModel;
    DatabaseReference offersCouponsForms;
    FirebaseDatabase database;
    private Button scBtnCurrent;
    String userNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
// init paper
        Paper.init(this);
        // init firebase
        database = FirebaseDatabase.getInstance();
        offersCouponsForms = database.getReference("MyOffers&Coupons");

        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        myCountDownTimer = new MyCountDownTimer(60000, 1000);
        userNo = Paper.book().read(Common.USER_KEY);
        //  myCountDownTimer.start();
        /*    setSupportActionBar((Toolbar) findViewById(R.id.toolbar_sudoku));*/
        img_back = findViewById(R.id.img_back);
        txt_countDown = findViewById(R.id.txt_countDown);
        Button btn = findViewById(R.id.btn);
        Button btn1 = findViewById(R.id.btn1);
        Button btn2 = findViewById(R.id.btn2);
        Button btn3 = findViewById(R.id.btn3);
        Button btn4 = findViewById(R.id.btn4);
        Button btn5 = findViewById(R.id.btn5);
        Button btn6 = findViewById(R.id.btn6);
        Button btn7 = findViewById(R.id.btn7);
        Button btn8 = findViewById(R.id.btn8);
        Button btn9 = findViewById(R.id.btn9);
        Button scBtn5 = findViewById(R.id.scBtn5);
        Button scBtn10 = findViewById(R.id.scBtn10);
        Button scBtn15 = findViewById(R.id.scBtn15);
        Button scBtn20 = findViewById(R.id.scBtn20);
        Button scBtn25 = findViewById(R.id.scBtn25);
        Button newGame = findViewById(R.id.newGameBtn);
        btn.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        scBtn5.setOnClickListener(this);
        scBtn10.setOnClickListener(this);
        scBtn15.setOnClickListener(this);
        scBtn20.setOnClickListener(this);
        scBtn25.setOnClickListener(this);
        newGame.setOnClickListener(this);
        setSC(15, scBtn15.getId());
        //  setSC(5, scBtn5.getId());
        startGame();

        img_back.setOnClickListener(v -> {
            myCountDownTimer.cancel();
            finish();
        });
    }
    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            int progress = (int) (millisUntilFinished/1000);

            progressBar.setProgress(progressBar.getMax()-progress);
            i--;
            txt_countDown.setText(""+i);
        }

        @Override
        public void onFinish() {
            // finish();
            txt_countDown.setText("0");
            setClickable(false);
            new AlertDialog.Builder(GameActivity.this)
                    .setTitle(getString(R.string.start_new_game))
                    .setMessage(getString(R.string.are_you_sure_again))
                    .setIcon(R.drawable.ic_info_black_24dp)
                    .setPositiveButton(R.string.try_again, (dialog, which) -> {

                        myCountDownTimer.cancel();
                        progressBar.setMax(0);
                        startGame();

                    })
                    .setNegativeButton(android.R.string.no, (dialog, which) -> dialog.dismiss()).show();
            // Toast.makeText(getApplicationContext(), getString(R.string.time_over), Toast.LENGTH_SHORT).show();
        }
    }
    private void startGame(){
        sg = new SudokuGenerator(spacesCount);
        sudoku = sg.getSudoku();
        isSolved = false;
        drawGrid(sudoku);
        setClickable(true);
        i = 60;
        progressBar.setMax(60);
        myCountDownTimer.start();
    }
    private void congrats(){
        myCountDownTimer.cancel();
        progressBar.setMax(0);

        String mobileNo = userNo;
        String strLastFourDi = mobileNo.length() >= 2 ? mobileNo.substring(mobileNo.length() - 2): "";
        SimpleDateFormat simple1 = new SimpleDateFormat("mmss");
        String key1 = simple1.format(new Date());
        String couponCode = "DNG"+strLastFourDi+""+key1;

        new AlertDialog.Builder(GameActivity.this)
                .setCancelable(false)
                .setTitle(getString(R.string.congrats))
                .setMessage("You won a discount coupon on next order use Coupon Code : "+couponCode)
                .setIcon(R.drawable.ic_local_offer_black_24dp)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd,hh:mm:ss");
                        String dateTime = simpleDateFormat.format(new Date());

                        SimpleDateFormat simple = new SimpleDateFormat("yyyyMMddhhmmss");
                        String key = simple.format(new Date());

                        DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
                        String time = dateFormat.format(new Date());
                        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
                                           /* SimpleDateFormat simpleDate = new SimpleDateFormat
                                                    ("dd-MM-yyyy");*/
                        String date = simpleDate.format(new Date());
                        String keyId = userNo+"_"+couponCode;

                        offersCouponsModel = new OffersCouponsModel(
                                "Game Coupon", "wining coupon", date, time,
                                dateTime, couponCode, "Active",userNo, "U","10",
                                "","","");
                        offersCouponsForms.child(keyId).setValue(offersCouponsModel);
                        // startGame();

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
        //  Toast.makeText(this, getString(R.string.congrats), Toast.LENGTH_SHORT).show();
        isSolved = true;
        setClickable(false);
    }
    private void setClickable(boolean clickable){
        TableLayout parent;
        TableRow tr;
        Button btn;

        parent= findViewById(R.id.parent_layout);
        for (int i = 0; i < parent.getChildCount(); i++) {
            if (i !=3 && i !=7) {
                tr = (TableRow) parent.getChildAt(i);
                for (int j = 0; j < tr.getChildCount(); j++) {
                    if (j != 3 && j!=7){
                        btn = (Button) tr.getChildAt(j);
                        if (!clickable) btn.setBackgroundResource(R.drawable.soluted_grid_button);
                        btn.setClickable(clickable);
                    }
                }
            }
        }
        parent = findViewById(R.id.digit_buttons);
        for (int i = 0; i < parent.getChildCount()-2; i++) {
            tr = (TableRow) parent.getChildAt(i);
            for (int j = 0; j < tr.getChildCount(); j++) {
                btn = (Button) tr.getChildAt(j);
                btn.setClickable(clickable);
            }
        }
    }
    private void drawGrid(int[][] mat){
        TableLayout parent = findViewById(R.id.parent_layout);
        parent.removeAllViews();
        TableRow row;
        Button btn;

        for (int i = 0; i < 9; i++) {
            row = new TableRow(this);
            row.setId(200000+i);
            row.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
            parent.addView(row);
            for (int j = 0; j < 9; j++) {
                btn = new Button(this);
                btn.setId(300000 + i*10 +j);
                TableRow.LayoutParams params = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        1.0f);
                btn.setLayoutParams(params);
                if (mat[i][j] == 0){
                    btn.setText("");
                    btn.setClickable(true);
                    btn.setBackgroundResource(R.drawable.clickable_grid_button);
                    btn.setOnClickListener(this);
                }else{
                    btn.setText(String.valueOf(mat[i][j]));
                    btn.setClickable(false);
                    btn.setBackgroundResource(R.drawable.unclickable_grid_button);
                }
                row.addView(btn);
                if (j == 2 || j == 5){
                    btn = new Button(this);
                    btn.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT,
                            1.73f));
                    btn.setClickable(false);
                    btn.setBackgroundResource(R.drawable.vertical_spacer);
                    row.addView(btn);
                }
            }
            if (i == 2 || i == 5){
                TableRow spacer = new TableRow(this);
                spacer.setLayoutParams(new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT,
                        1.0f));
                TextView space = new TextView(this);
                space.setTextSize(TypedValue.COMPLEX_UNIT_SP, 3);
                spacer.addView(space);
                parent.addView(spacer);
            }
        }
    }
    private void setDigit(int digit){
        if (currentBtn != null) {
            currentBtn.setText((digit == 0) ? "" : String.valueOf(digit));
            int i = ((currentBtn.getId()) - 300000) / 10;
            int j = ((currentBtn.getId()) - 300000) % 10;
            sudoku[i][j] = digit;
            if (checkSolution()) {
                congrats();
            }
        }
    }

    boolean useInSquare(int digit_i, int digit_j, int digit){
        int rowStart = (digit_i/3)*3;
        int colStart = (digit_j/3)*3;
        for (int i = rowStart; i < rowStart+3; i++) {
            for (int j = colStart; j < colStart+3; j++) {
                if (sudoku[i][j] == digit && i != digit_i && j != digit_j){
                    //Toast.makeText(this, "false in box " + sudoku[i][j] + " == " + digit + " on i:" + i + " and j:" + j , Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        return true;
    }
    boolean useInRow(int row, int col,  int digit){
        for (int i = 0; i < 9; i++) {
            if (sudoku[row][i] == digit && i != col){
                //Toast.makeText(this, "false in row " + sudoku[row][i] + " == " + digit + " on i:" + row + " and j:" + i , Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }
    boolean useInColumn(int row, int col, int digit){
        for (int i = 0; i < 9; i++) {
            if (sudoku[i][col] == digit && i != row){
                //Toast.makeText(this, "false in column " + sudoku[i][col] + " == " + digit + " on i:" + i + " and j:" + col , Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }
    private boolean checkSolution() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (sudoku[i][j] == 0){
                    return false;
                }
            }
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (!useInRow(i, j, sudoku[i][j])
                        || !useInColumn(i, j, sudoku[i][j])
                        || !useInSquare(i, j, sudoku[i][j])
                ){
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn:
                setDigit(0);
                break;
            case R.id.btn1:
                setDigit(1);
                break;
            case R.id.btn2:
                setDigit(2);
                break;
            case R.id.btn3:
                setDigit(3);
                break;
            case R.id.btn4:
                setDigit(4);
                break;
            case R.id.btn5:
                setDigit(5);
                break;
            case R.id.btn6:
                setDigit(6);
                break;
            case R.id.btn7:
                setDigit(7);
                break;
            case R.id.btn8:
                setDigit(8);
                break;
            case R.id.btn9:
                setDigit(9);
                break;
            case R.id.scBtn5:
                setSC(5, v.getId());
                break;
            case R.id.scBtn10:
                setSC(10, v.getId());
                break;
            case R.id.scBtn15:
                setSC(15, v.getId());
                break;
            case R.id.scBtn20:
                setSC(20, v.getId());
                break;
            case R.id.scBtn25:
                setSC(25, v.getId());
                break;
            case R.id.newGameBtn:
                if (!isSolved) {
                    new AlertDialog.Builder(this)
                            .setTitle(getString(R.string.start_new_game))
                            .setMessage(getString(R.string.are_ypu_sure))
                            .setIcon(R.drawable.ic_info_black_24dp)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startGame();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                }else{
                    startGame();
                }
                break;
            default:
                if (currentBtn != null){
                    currentBtn.setBackgroundResource(R.drawable.clickable_grid_button);
                }
                currentBtn = findViewById(v.getId());
                currentBtn.setBackgroundResource(R.drawable.current_grid_button);
                break;
        }
    }
    private void setSC(int sc, int btnId) {
        if (scBtnCurrent != null){
            scBtnCurrent.setBackgroundResource(R.drawable.sc_button);
        }
        scBtnCurrent = findViewById(btnId);
        scBtnCurrent.setBackgroundResource(R.drawable.sc_button_current);
        spacesCount = sc;
    }
}
