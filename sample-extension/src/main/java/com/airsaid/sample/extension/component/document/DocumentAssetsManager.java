package com.airsaid.sample.extension.component.document;

import android.content.Context;
import android.content.res.AssetManager;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JackChen
 */
public class DocumentAssetsManager {
  private final static DocumentAssetsManager documentAssetsManager = new DocumentAssetsManager();

  public static DocumentAssetsManager getInstance() {
    return documentAssetsManager;
  }

  private List<String> documentFileList = new ArrayList<>();

  public void onCreate(Context context) {
    AssetManager assets = context.getAssets();
    collectDocumentFiles(assets, documentFileList, "document");
  }

  private void collectDocumentFiles(AssetManager assets, List<String> documentFileList, String filePath) {
    try {
      if (filePath.endsWith(".md") || filePath.endsWith(".MD")) {
        documentFileList.add(filePath);
      } else {
        //Folder
        String[] fileList = assets.list(filePath);
        if (null != fileList) {
          for (String fileName : fileList) {
            collectDocumentFiles(assets, documentFileList, filePath + "/" + fileName);
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Nullable
  public String findDocument(String path) {
    String documentPath = null;
    for (String documentFilePath : documentFileList) {
      if (documentFilePath.endsWith(path)) {
        documentPath = documentFilePath;
        break;
      }
    }
    return documentPath;

  }
}
