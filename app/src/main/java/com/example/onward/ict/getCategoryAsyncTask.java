package com.example.onward.ict;

import android.os.AsyncTask;

abstract class getCategoryAsyncTask extends AsyncTask<Void, Category, String> {
    protected abstract void onPostExecute(String s, Category category);
}
