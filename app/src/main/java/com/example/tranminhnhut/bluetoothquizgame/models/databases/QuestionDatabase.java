package com.example.tranminhnhut.bluetoothquizgame.models.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tranminhnhut.bluetoothquizgame.models.AnswerModel;
import com.example.tranminhnhut.bluetoothquizgame.models.CorrectAnswerModel;
import com.example.tranminhnhut.bluetoothquizgame.models.QuestionModel;
import com.example.tranminhnhut.bluetoothquizgame.models.UserModel;

import java.util.ArrayList;

//Database SQLite
public class QuestionDatabase extends SQLiteOpenHelper {
    private static final String TAG = "SQLite";
    private static final String DATABASE_NAME = "Question";
    private static final int DATABASE_VERSION = 3;

    private static final String TABLE_1 = "Question";
    private static final String TABLE_2 = "Answer";
    private static final String TABLE_3 = "CorrectAnswer";
    private static final String TABLE_4 = "User_Info";

    // key
    private static final String Question = "Question";
    private static final String Answer = "Answer";
    private static final String Correct = "CorrectAnswer";

    // Value
    private static final String info = "_info";
    private static final String question = "_question";
    private static final String pos = "_pos";
    private static final String type = "_type";
    private static final String name = "_name";
    private static final String result = "_result";

    //Total
    public static final int TOTAL_QUESTION = 10; // tổng số câu hỏi
    public static final int TOTAL_TYPE = 3; // tổng số thể loại câu hỏi

    public QuestionDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        //Tạo bảng
        String script_1 = String.format("CREATE TABLE %s(%s TEXT PRIMARY KEY, %s TEXT, %s INTEGER, %s TEXT)", TABLE_1, Question, info, pos, type);
        String script_2 = String.format("CREATE TABLE %s(%s TEXT PRIMARY KEY, %s TEXT, %s TEXT)", TABLE_2, Answer, question, info);
        String script_3 = String.format("CREATE TABLE %s(%s TEXT PRIMARY KEY, %s TEXT)", TABLE_3, Correct, question);
        String script_4 = String.format("CREATE TABLE %s(%s TEXT, %s TEXT)", TABLE_4, name, result);

        db.execSQL(script_1);
        db.execSQL(script_2);
        db.execSQL(script_3);
        db.execSQL(script_4);

        createDefaultDatabase(db);

    }

    //Update dữ liệu
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String drop_1 = "DROP TABLE IF EXISTS " + TABLE_1;
        String drop_2 = "DROP TABLE IF EXISTS " + TABLE_2;
        String drop_3 = "DROP TABLE IF EXISTS " + TABLE_3;

        db.execSQL(drop_1);
        db.execSQL(drop_2);
        db.execSQL(drop_3);

        onCreate(db);
    }

    // Tạo dữ liệu trong database
    public void createDefaultDatabase(SQLiteDatabase db){
        //Question
        insertQuestionTable(db, new QuestionModel("Q1", "Một cộng một bằng mấy ?", 1, "Toán"));
        //Answer
        insertAnswerTable(db, new AnswerModel("Q1", "A_Q1", "Một"));
        insertAnswerTable(db, new AnswerModel("Q1", "B_Q1", "Hai"));
        insertAnswerTable(db, new AnswerModel("Q1", "C_Q1", "Ba"));
        insertAnswerTable(db, new AnswerModel("Q1", "D_Q1", "Bốn"));
        //Correct answer
        insertCorrectTable(db, new CorrectAnswerModel("Q1", "B_Q1"));
    }

    //insert câu hỏi
    public void insertQuestionTable(SQLiteDatabase db, QuestionModel questionModel){
        ContentValues values = new ContentValues();

        values.put(Question, questionModel.getID_Question());
        values.put(info, questionModel.getInfo());
        values.put(pos, questionModel.getPos());
        values.put(type, questionModel.getType());
        db.insert(TABLE_1, null, values);

    }
    //insert câu trả lời
    public void insertAnswerTable(SQLiteDatabase db, AnswerModel answerModel){
        ContentValues values = new ContentValues();

        values.put(question, answerModel.getID_Answer());
        values.put(info, answerModel.getInfo());
        values.put(Answer, answerModel.getID_Answer());

        db.insert(TABLE_2, null, values);
    }
    //insert câu trả lời đúng
    public void insertCorrectTable(SQLiteDatabase db, CorrectAnswerModel correctAnswerModel){
        ContentValues values = new ContentValues();

        values.put(Correct, correctAnswerModel.getID_Correct());
        values.put(question, correctAnswerModel.getID_Question());

        db.insert(TABLE_3, null, values);
    }

    public void insertUserinfo(UserModel userModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(name, userModel.getComponentName());
        values.put(result, userModel.getResult());

        db.insert(TABLE_4, null, values);
        db.close();
    }

    //Lấy câu hỏi với id và thể loại
    public QuestionModel getQuestion(int id, String qType){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        QuestionModel questionModel = null;

        Cursor cursor = sqLiteDatabase.query(TABLE_1, new String[]{Question, info, pos, type}, pos + "='"+ String.valueOf(id) + "' AND " + type + "='" + qType + "'",null, null, null, null);

        if (cursor.moveToFirst()) {
            questionModel = new QuestionModel(cursor.getString(0), cursor.getString(1), Integer.parseInt(cursor.getString(2)), cursor.getString(3));
        }

        cursor.close();
        sqLiteDatabase.close();
        return questionModel;
    }
    //Lấy câu trả lời tương ứng với câu hỏi
    public ArrayList<AnswerModel> getAnswer(String ID_Question){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        ArrayList<AnswerModel> answerModels = new ArrayList<AnswerModel>();

        Cursor cursor = sqLiteDatabase.query(TABLE_2, new String[]{question, Answer, info}, question + "='"+ ID_Question + "'", null, null, null, null);

        if (cursor.moveToFirst()){
            do{
                AnswerModel answerModel = new AnswerModel();
                answerModel.setID_Question(cursor.getString(0));
                answerModel.setID_Answer(cursor.getString(1));
                answerModel.setInfo(cursor.getString(2));
                answerModels.add(answerModel);
            } while (cursor.moveToNext());
        }

        cursor.close();
        sqLiteDatabase.close();
        return answerModels;
    }
    //Lấy câu trả lời chính xác của câu hỏi
    public CorrectAnswerModel getCorrectAnswer(String ID_Question){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        CorrectAnswerModel correctAnswerModel = null;
        Cursor cursor = sqLiteDatabase.query(TABLE_3, new String[]{Correct, question}, question + "='"+ ID_Question + "'", null, null, null, null);

        if (cursor.moveToFirst())
            correctAnswerModel = new CorrectAnswerModel(cursor.getString(1), cursor.getString(0));

        cursor.close();
        sqLiteDatabase.close();
        return correctAnswerModel;
    }

    public ArrayList<UserModel> getAllUserinfo(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        ArrayList<UserModel> userModels = new ArrayList<UserModel>();

        String query = "SELECT * FROM " + TABLE_4;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if (cursor.moveToFirst()){
            do{
                UserModel userModel = new UserModel();
                userModel.setComponentName(cursor.getString(0));
                userModel.setResult(cursor.getString(1));
                userModels.add(userModel);
            } while (cursor.moveToNext());
        }

        cursor.close();
        sqLiteDatabase.close();
        return userModels;
    }
}
