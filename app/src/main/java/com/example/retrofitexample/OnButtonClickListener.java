package com.example.retrofitexample;

import android.widget.Button;
import android.widget.ProgressBar;

public interface OnButtonClickListener {
    public void onDownloadBtnClicked(String str1, String str2, Button downloadBtn, ProgressBar progress, boolean download_progress);
}
