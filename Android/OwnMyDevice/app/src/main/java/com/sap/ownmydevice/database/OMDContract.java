package com.sap.ownmydevice.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.sap.ownmydevice.models.Member;
import com.sap.ownmydevice.models.Team;

import java.util.ArrayList;

/**
 * Created by Harshit Pathak on 07/08/15.
 * OMDContract Specifies the Schema and provides an API to access the DB
 */
public  final class OMDContract {

    private static DBStore mDbHelper ;
    private static OMDContract instance;

    private OMDContract(Context context){
        mDbHelper = new DBStore(context);
    };

    public static OMDContract getDBConnection(Context context){
        if(instance == null){
           instance =  new OMDContract(context);
        }
        return instance;
    }

    //DB API
    public long insertTeam(Team team){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TeamTable.COLUMN_NAME_TEAM_NAME, team.getTeamName());
        values.put(TeamTable.COLUMN_NAME_TEAM_URL, team.getUrl());
        values.put(TeamTable.COLUMN_NAME_TEAM_ID,team.getId());
        ArrayList<Member> members = team.getMembers();
        if(members!=null) {
            if (members.size() > 0) {
                for (int i = 0; i < members.size(); i++) {
                    long id = insertMember(members.get(i));
                    if (id < 0) {
                        return -1;
                    }
                }
            }
        }
        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                TeamTable.TABLE_NAME,
                null,
                values);

        return newRowId;
    }

    public long insertMember(Member member){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MemberTable.COLUMN_NAME_MEMBER_URL, member.getUrl());
        values.put(MemberTable.COLUMN_NAME_MEMBER_ID, member.getUrl());
        values.put(MemberTable.COLUMN_NAME_MEMBER_NAME, member.getMemberName());
        values.put(MemberTable.COLUMN_NAME_MEMBER_EID, member.getMemberEID());
        values.put(MemberTable.COLUMN_NAME_MEMBER_EMAIL, member.getMemberEmail());
        values.put(MemberTable.COLUMN_NAME_MEMBER_TEAM, member.getMemberTeam());

        long newMemberId;
        newMemberId = db.insert(MemberTable.TABLE_NAME,null,values);

        return newMemberId;
    }

    public ArrayList<Team> getTeamList(){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                TeamTable._ID,
                TeamTable.COLUMN_NAME_TEAM_ID,
                TeamTable.COLUMN_NAME_TEAM_URL,
                TeamTable.COLUMN_NAME_TEAM_NAME

        };

        String sortOrder =
                TeamTable.COLUMN_NAME_TEAM_NAME + " ASC";

        Cursor c = db.query(
                TeamTable.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        c.moveToFirst();
        ArrayList<Team> teams = new ArrayList<Team>();
        while(!c.isAfterLast()){
            String url = c.getString(c.getColumnIndex(TeamTable.COLUMN_NAME_TEAM_URL));
            String name = c.getString(c.getColumnIndex(TeamTable.COLUMN_NAME_TEAM_NAME));
            String id = c.getString(c.getColumnIndex(TeamTable.COLUMN_NAME_TEAM_ID));
            Team team = new Team(url,id,name);
            teams.add(team);
            c.moveToNext();
        }
        return teams;
    }

    //Since Members would be fetched for particular teams hence teamIdentifier is required
    public ArrayList<Member> getMembers(String teamIdentifier)
    {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                MemberTable._ID,
                MemberTable.COLUMN_NAME_MEMBER_URL,
                MemberTable.COLUMN_NAME_MEMBER_ID,
                MemberTable.COLUMN_NAME_MEMBER_NAME,
                MemberTable.COLUMN_NAME_MEMBER_EID,
                MemberTable.COLUMN_NAME_MEMBER_EMAIL,
                MemberTable.COLUMN_NAME_MEMBER_TEAM,


        };

        String selection =  MemberTable.COLUMN_NAME_MEMBER_TEAM + " =? " ;
        String[] selectionArgs = {teamIdentifier};

        String sortOrder =
                MemberTable.COLUMN_NAME_MEMBER_NAME + " ASC";

        Cursor c = db.query(
                MemberTable.TABLE_NAME,               // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        c.moveToFirst();
        ArrayList<Member> members = new ArrayList<Member>();
        while(!c.isAfterLast()){
            String url = c.getString(c.getColumnIndex(MemberTable.COLUMN_NAME_MEMBER_URL));
            String id = c.getString(c.getColumnIndex(MemberTable.COLUMN_NAME_MEMBER_ID));
            String name = c.getString(c.getColumnIndex(MemberTable.COLUMN_NAME_MEMBER_NAME));
            String eid = c.getString(c.getColumnIndex(MemberTable.COLUMN_NAME_MEMBER_EID));
            String email = c.getString(c.getColumnIndex(MemberTable.COLUMN_NAME_MEMBER_EMAIL));
            String team = c.getString(c.getColumnIndex(MemberTable.COLUMN_NAME_MEMBER_TEAM));
            Member member = new Member(url,name,eid,email,team);
            members.add(member);
            c.moveToNext();
        }
        return members;

    }

    public Team getTeam(String teamIdentifier){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                TeamTable._ID,
                TeamTable.COLUMN_NAME_TEAM_URL,
                TeamTable.COLUMN_NAME_TEAM_ID,
                TeamTable.COLUMN_NAME_TEAM_NAME

        };
        String selection = TeamTable.COLUMN_NAME_TEAM_ID + " =? " ;
        String [] selectionArgs = {teamIdentifier};
        String sortOrder =
                TeamTable.COLUMN_NAME_TEAM_NAME + " ASC";

        Cursor c = db.query(
                TeamTable.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        c.moveToFirst();
        Team team = null;
        if(!c.isAfterLast()) {
            team = new Team(c.getString(c.getColumnIndex(TeamTable.COLUMN_NAME_TEAM_URL)),
                    c.getString(c.getColumnIndex(TeamTable.COLUMN_NAME_TEAM_ID)),
                    c.getString(c.getColumnIndex(TeamTable.COLUMN_NAME_TEAM_NAME)));
        }
        return team;
    }

    public Member getMember(String memberIdentifier)
    {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                MemberTable._ID,
                MemberTable.COLUMN_NAME_MEMBER_URL,
                MemberTable.COLUMN_NAME_MEMBER_ID,
                MemberTable.COLUMN_NAME_MEMBER_NAME,
                MemberTable.COLUMN_NAME_MEMBER_EID,
                MemberTable.COLUMN_NAME_MEMBER_EMAIL,
                MemberTable.COLUMN_NAME_MEMBER_TEAM,

        };

        String selection = MemberTable.COLUMN_NAME_MEMBER_ID + "=?";
        String[] selectionArgs = {memberIdentifier};

        String sortOrder =
                MemberTable.COLUMN_NAME_MEMBER_NAME + " ASC";

        Cursor c = db.query(
                MemberTable.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,      // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        c.moveToFirst();
        Member member = null;
        if(!c.isAfterLast()){
            String url = c.getString(c.getColumnIndex(MemberTable.COLUMN_NAME_MEMBER_URL));
            String id = c.getString(c.getColumnIndex(MemberTable.COLUMN_NAME_MEMBER_ID));
            String name = c.getString(c.getColumnIndex(MemberTable.COLUMN_NAME_MEMBER_NAME));
            String eid = c.getString(c.getColumnIndex(MemberTable.COLUMN_NAME_MEMBER_EID));
            String email = c.getString(c.getColumnIndex(MemberTable.COLUMN_NAME_MEMBER_EMAIL));
            String team = c.getString(c.getColumnIndex(MemberTable.COLUMN_NAME_MEMBER_TEAM));
            member = new Member(url,id,name,eid,email,team);

        }
        return member;
    }

    public int updateTeam(Team team){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(TeamTable.COLUMN_NAME_TEAM_URL, team.getUrl());
        values.put(TeamTable.COLUMN_NAME_TEAM_NAME, team.getTeamName());

        // Which row to update, based on the ID
        String selection = TeamTable.COLUMN_NAME_TEAM_ID + "=?";
        String[] selectionArgs = { team.getId() };

        int count = db.update(
                TeamTable.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        return count;

    }

    public int updateMember(Member member){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(MemberTable.COLUMN_NAME_MEMBER_URL, member.getUrl());
        values.put(MemberTable.COLUMN_NAME_MEMBER_NAME, member.getMemberName());
        values.put(MemberTable.COLUMN_NAME_MEMBER_EID, member.getMemberEID());
        values.put(MemberTable.COLUMN_NAME_MEMBER_EMAIL, member.getMemberEmail());
        values.put(MemberTable.COLUMN_NAME_MEMBER_TEAM, member.getMemberTeam());
        // Which row to update, based on the ID
        String selection = MemberTable.COLUMN_NAME_MEMBER_ID + "=?";
        String[] selectionArgs = { member.getId() };

        int count = db.update(
                MemberTable.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        return count;

    }
    //DB API END


    private static abstract class TeamTable implements BaseColumns{
        public static final String TABLE_NAME = "team";
        public static final String COLUMN_NAME_TEAM_ID = "id";
        public static final String COLUMN_NAME_TEAM_URL = "url";
        public static final String COLUMN_NAME_TEAM_NAME = "team_name";

    }

    private static abstract class MemberTable implements BaseColumns{
        public static final String TABLE_NAME = "member";
        public static final String COLUMN_NAME_MEMBER_URL = "url";
        public static final String COLUMN_NAME_MEMBER_ID = "id";
        public static final String COLUMN_NAME_MEMBER_NAME = "member_name";
        public static final String COLUMN_NAME_MEMBER_EID = "member_eid";
        public static final String COLUMN_NAME_MEMBER_EMAIL = "member_email";
        public static final String COLUMN_NAME_MEMBER_TEAM = "member_team";
    }

    private class DBStore extends SQLiteOpenHelper {
        private static final String TEXT_TYPE = " TEXT";
        private static final String COMMA_SEP = ",";
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "OMD.db";

        private static final String SQL_CREATE_TEAM =
                "CREATE TABLE " + OMDContract.TeamTable.TABLE_NAME + " (" +
                        OMDContract.TeamTable._ID + " INTEGER PRIMARY KEY," +
                        OMDContract.TeamTable.COLUMN_NAME_TEAM_URL + TEXT_TYPE + COMMA_SEP +
                        OMDContract.TeamTable.COLUMN_NAME_TEAM_ID + TEXT_TYPE +COMMA_SEP+
                        OMDContract.TeamTable.COLUMN_NAME_TEAM_NAME + TEXT_TYPE +

                        " )";

        private static final String SQL_CREATE_MEMBER =
                "CREATE TABLE " + OMDContract.MemberTable.TABLE_NAME + " (" +
                        OMDContract.MemberTable._ID + " INTEGER PRIMARY KEY," +
                        OMDContract.MemberTable.COLUMN_NAME_MEMBER_URL + TEXT_TYPE + COMMA_SEP +
                        MemberTable.COLUMN_NAME_MEMBER_ID + TEXT_TYPE +COMMA_SEP+
                        OMDContract.MemberTable.COLUMN_NAME_MEMBER_NAME + TEXT_TYPE + COMMA_SEP +
                        OMDContract.MemberTable.COLUMN_NAME_MEMBER_EID + TEXT_TYPE + COMMA_SEP +
                        OMDContract.MemberTable.COLUMN_NAME_MEMBER_EMAIL + TEXT_TYPE + COMMA_SEP +
                        OMDContract.MemberTable.COLUMN_NAME_MEMBER_TEAM + TEXT_TYPE  +

                        " )";


        private static final String SQL_DELETE_TEAMS =
                "DROP TABLE IF EXISTS " + OMDContract.TeamTable.TABLE_NAME;
        private static final String SQL_DELETE_MEMBERS =
                "DROP TABLE IF EXISTS " + OMDContract.MemberTable.TABLE_NAME;


        public DBStore(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_TEAM);
            db.execSQL(SQL_CREATE_MEMBER);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_TEAMS);
            db.execSQL(SQL_DELETE_MEMBERS);
            onCreate(db);
        }
    }

}
