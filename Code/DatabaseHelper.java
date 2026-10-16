package com.example.rentalvilla;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "RentalVilla.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_OWNER = "Owner";
    private static final String TABLE_USERS = "Users";
    private static final String TABLE_CARS = "Cars";
    private static final String TABLE_BOOKINGS = "Bookings";
    private static final String TABLE_FEEDBACK = "Feedback";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_OWNER + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, Username TEXT, Password TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_USERS + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT, Email TEXT UNIQUE, Password TEXT, Phone TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_CARS + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, CarName TEXT, CarNumber TEXT, PricePerDay REAL, Details TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_BOOKINGS + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, UserID INTEGER, CarID INTEGER, StartDate TEXT, EndDate TEXT, PaymentStatus TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_FEEDBACK + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, UserID INTEGER, Message TEXT)");

        db.execSQL("INSERT INTO " + TABLE_CARS + "(CarName, CarNumber, PricePerDay, Details) VALUES " +
                "('Toyota Corolla', 'US-011', 5000, 'Comfortable sedan'), " +
                "('Honda Civic', 'CL-712', 6000, 'Sporty and efficient'), " +
                "('Suzuki Alto', 'AQE-986', 3000, 'Smooth and Easy'), " +
                "('Toyota Xli', 'BKL-011', 4500, 'Comfy'), " +
                "('Honda City', 'CV-785', 5000, 'Comfartable'), " +
                "('Toyota Surf', 'LV-1', 7000, '4x4'), " +
                "('Suzuki Every', 'Su-6787', 4000, 'Smooth'), " +
                "('Toyota Hiace', 'BKL-6743', 8000, 'For Family'), " +
                "('Suzuki Cultux Vx', 'CL-553', 3500, 'Petrol Relaiable'), " +
                "('Suzuki Wagon R', 'JKL-456', 4000, 'Economical hatchback')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OWNER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEEDBACK);
        onCreate(db);
    }

    // Owner
    public boolean registerOwner(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_OWNER, null);
        if (cursor.getCount() > 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        ContentValues values = new ContentValues();
        values.put("Username", username);
        values.put("Password", password);
        long result = db.insert(TABLE_OWNER, null, values);
        return result != -1;
    }

    public boolean loginOwner(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_OWNER + " WHERE Username = ? AND Password = ?",
                new String[]{username, password}
        );
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean isOwnerExists() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_OWNER, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Users
    public boolean registerUser(String name, String email, String password, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Name", name);
        values.put("Email", email);
        values.put("Password", password);
        values.put("Phone", phone);
        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public boolean loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_USERS + " WHERE Email = ? AND Password = ?",
                new String[]{email, password}
        );
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT ID, Name, Email, Phone FROM " + TABLE_USERS, null);
    }

    // ✅ Correct conflict check for booking
    // ✅ Correct getAvailableCars: only cars with NO overlap appear
    public Cursor getAvailableCars(String startDate, String endDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CARS + " WHERE ID NOT IN (" +
                "SELECT CarID FROM " + TABLE_BOOKINGS + " WHERE NOT (" +
                "date(EndDate) < date(?) OR date(StartDate) > date(?)))";
        return db.rawQuery(query, new String[]{startDate, endDate});
    }

    // ✅ Correct bookCar: do not book if overlap exists
    public boolean bookCar(String userEmail, int carId, String startDate, String endDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT ID FROM " + TABLE_USERS + " WHERE Email = ?", new String[]{userEmail});
        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }
        cursor.close();
        if (userId == -1) return false;

        Cursor conflict = db.rawQuery(
                "SELECT * FROM " + TABLE_BOOKINGS + " WHERE CarID = ? AND NOT (" +
                        "date(EndDate) < date(?) OR date(StartDate) > date(?))",
                new String[]{String.valueOf(carId), startDate, endDate}
        );
        boolean hasConflict = conflict.getCount() > 0;
        conflict.close();
        if (hasConflict) return false;

        ContentValues values = new ContentValues();
        values.put("UserID", userId);
        values.put("CarID", carId);
        values.put("StartDate", startDate);
        values.put("EndDate", endDate);
        values.put("PaymentStatus", "Pending");
        long result = db.insert(TABLE_BOOKINGS, null, values);
        return result != -1;
    }


    public Cursor getAllBookings() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT B.ID, U.Name AS UserName, C.CarName, C.CarNumber, C.PricePerDay, B.StartDate, B.EndDate, B.PaymentStatus " +
                "FROM " + TABLE_BOOKINGS + " B " +
                "JOIN " + TABLE_USERS + " U ON B.UserID = U.ID " +
                "JOIN " + TABLE_CARS + " C ON B.CarID = C.ID";
        return db.rawQuery(query, null);
    }

    // ✅ NEW: get bookings for a specific user
    // ✅ New method to get bookings for a specific user by their email
    public Cursor getUserBookings(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Get the user's ID first
        Cursor cursor = db.rawQuery("SELECT ID FROM " + TABLE_USERS + " WHERE Email = ?", new String[]{email});
        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }
        cursor.close();
        if (userId == -1) return null;

        // Return only this user's bookings
        String query = "SELECT B.ID, C.CarName, C.CarNumber, C.PricePerDay, B.StartDate, B.EndDate, B.PaymentStatus " +
                "FROM " + TABLE_BOOKINGS + " B " +
                "JOIN " + TABLE_CARS + " C ON B.CarID = C.ID " +
                "WHERE B.UserID = ?";
        return db.rawQuery(query, new String[]{String.valueOf(userId)});
    }



    public boolean updatePaymentStatus(int bookingId, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("PaymentStatus", newStatus);
        int result = db.update(TABLE_BOOKINGS, values, "ID = ?", new String[]{String.valueOf(bookingId)});
        return result > 0;
    }

    public boolean addFeedback(String userEmail, String message) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT ID FROM " + TABLE_USERS + " WHERE Email = ?", new String[]{userEmail});
        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }
        cursor.close();
        if (userId == -1) return false;

        ContentValues values = new ContentValues();
        values.put("UserID", userId);
        values.put("Message", message);
        long result = db.insert(TABLE_FEEDBACK, null, values);
        return result != -1;
    }

    public Cursor getAllFeedback() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT F.ID, U.Name AS UserName, F.Message " +
                "FROM " + TABLE_FEEDBACK + " F " +
                "JOIN " + TABLE_USERS + " U ON F.UserID = U.ID";
        return db.rawQuery(query, null);
    }
}
