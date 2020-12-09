package gr.hua.dit.android.contactscontentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Console;

public class ContactsContentProvider extends ContentProvider {
    private static UriMatcher uriMatcher;
    private DbHelper dbHelper;
    private static final String AUTHORITY = "gr.hua.dit.android.contactscontentprovider";
    public static final String CONTENT_URI = "content://"+AUTHORITY;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY,"contacts",1);
        uriMatcher.addURI(AUTHORITY,"contacts/#",2);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor result = null;
        switch (uriMatcher.match(uri)){
            case 1:
                result = dbHelper.selectAll();
                break;
            case 2:
                result = dbHelper.selectContactById(Integer.parseInt(uri.getLastPathSegment()));
                break;
        }
        return result;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Uri result = null;
        switch(uriMatcher.match(uri)){
            case 1:
                ContactContract contact = new ContactContract(values.getAsString("name"),values.getAsString("phone"));
                long id = dbHelper.insertContact(contact);
                result = Uri.parse(AUTHORITY+"/contacts/"+id);
                break;
        }
        return result;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
