package com.kt.na_social.ultis;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class FileUtils {
    public static String getFileExtension(Context context, Uri uri) {
        String extension = null;

        // Get MIME type
        ContentResolver contentResolver = context.getContentResolver();
        String mimeType = contentResolver.getType(uri);

        if (mimeType != null) {
            // Convert MIME type to extension
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(mimeType);
        } else {
            // Handle cases where the MIME type is null
            String path = uri.getPath();
            if (path != null && path.contains(".")) {
                extension = path.substring(path.lastIndexOf(".") + 1);
            }
        }

        return extension;
    }

    public static List<MultipartBody.Part> beforeUpload(Context context, Uri... uris) throws Exception {

        List<MultipartBody.Part> fileParts = new ArrayList<>();
        for (Uri uri : uris) {
            File uploadFile = FileUtils.getFileFromUri(context, uri);
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), uploadFile);
            MultipartBody.Part body = MultipartBody.Part.createFormData("files", uploadFile.getName(), requestFile);
            fileParts.add(body);
        }
        return fileParts;
    }

    public static File getFileFromUri(Context context, Uri uri) throws Exception {
        ContentResolver contentResolver = context.getContentResolver();

        // Retrieve the file name
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        String fileName = null;
        if (cursor != null && cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            fileName = cursor.getString(nameIndex);
            cursor.close();
        }

        if (fileName == null) {
            throw new Exception("Unable to get file name");
        }

        File file = new File(context.getCacheDir(), fileName);
        try (InputStream inputStream = contentResolver.openInputStream(uri);
             FileOutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        return file;
    }
}
