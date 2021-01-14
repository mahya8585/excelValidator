package com.maaya.demo.excel.validator.helper;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.HashSet;
import java.util.Set;

@Component
public class AzureStorageHelper {
    //接続文字列
    //TODO 事前にexcelboxという名前のAzure storageを作成しておく。ストレージ名を変更した場合は下記account nameも変更する
    private static final String STORAGE_CONNECTION_STRING =
            "DefaultEndpointsProtocol=https;" +
            "AccountName=excelbox;" +
            "AccountKey=【Azure storage account keyを入力する】";

    /**
     * ファイルをアップロードする
     *
     * @param target        アップロード対象
     * @param containerName コンテナ名
     * @param fileName      ファイル名
     * @param length        アップロードファイル長
     * @throws URISyntaxException
     * @throws InvalidKeyException
     * @throws StorageException
     * @throws IOException
     */
    public void upload(InputStream target, String containerName, String fileName, int length) throws URISyntaxException, InvalidKeyException, StorageException, IOException {
        // BLOBを配置するコンテナーの設定.
        CloudBlobContainer container = createCloudBlobContainer(containerName);

        // もしすでに存在する場合は上書きされる
        CloudBlockBlob blob = container.getBlockBlobReference(fileName);
        blob.upload(target, length);

    }

    /**
     * コンテナー内に存在するファイルの一覧を取得する
     *
     * @return
     * @throws URISyntaxException
     * @throws InvalidKeyException
     * @throws StorageException
     */
    public Set<String> selectAll(String containerName) throws URISyntaxException, InvalidKeyException, StorageException {
        // BLOBを配置するコンテナーの設定.
        CloudBlobContainer container = createCloudBlobContainer(containerName);

        // Loop over blobs within the container and output the URI to each of them.
        final Set<String> urls = new HashSet<>();
        container.listBlobs().forEach(blob -> urls.add(blob.getUri().toString()));

        return urls;
    }

    /**
     * 指定BlobのURIを取得する
     *
     * @param containerName コンテナ名
     * @param blobName      取得したいBLOB名
     * @return
     * @throws StorageException
     * @throws InvalidKeyException
     * @throws URISyntaxException
     */
    public String selectBlobUri(String containerName, String blobName) throws StorageException, InvalidKeyException, URISyntaxException {
        //コンテナーの設定
        CloudBlobContainer container = createCloudBlobContainer(containerName);

        //ファイルURLの取得
        CloudBlockBlob blob = container.getBlockBlobReference(blobName);
        return blob.getUri().toString();
    }

    /**
     * Strage共通処理。コンテナー情報の取得
     *
     * @param containerName コンテナ名
     * @return
     * @throws URISyntaxException
     * @throws StorageException
     * @throws InvalidKeyException
     */
    private CloudBlobContainer createCloudBlobContainer(String containerName) throws URISyntaxException, StorageException, InvalidKeyException {
        // Retrieve storage account from connection-string.
        CloudStorageAccount storageAccount = createStorageAccount();

        // Create the blob client.
        CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

        // Retrieve reference to a previously created container.
        return blobClient.getContainerReference(containerName);
    }

    /**
     * 接続先設定
     *
     * @return 接続先設定
     * @throws URISyntaxException
     * @throws InvalidKeyException
     */
    private CloudStorageAccount createStorageAccount() throws URISyntaxException, InvalidKeyException {
        return CloudStorageAccount.parse(STORAGE_CONNECTION_STRING);
    }


}