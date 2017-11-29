# 商品一覧の作成
## 使用した技術要素
 - Java 1.8
 - Spring Boot 1.5.4
 - H2

## 全体の設計・構成についての説明

画面構成は、
1. 商品の一覧画面
2. 商品の紹介作成画面
3. 商品の詳細画面 
4. Twitter情報画面<br>

の4種類で構成されている。

## 開発環境のセットアップ手順
1. このプロジェクトを git clone する
2. プロジェクト内に移動し、 mvn spring-boot:run でSpringを起動する。
3. 起動後は、 localhost:8080 でページに移動できる。

## 画面ごとの内容

### 共通画面
ヘッダー部分に
 <li>一覧画面にアクセスする TOPタグ
 <li>作成画面にアクセスする CREATEタグ
 <li>Twitter画面にアクセスする TWITTERタグ
が全ての画面にあります。

### 権限について
apiについては、アクセストークンがなければ利用することができません。 <br>
アクセストークンを取得するには、Twitterのアカウントと連携しているため、
Twitterアカウントが必要です。<br>
アクセストークンはTwitterに接続した後、logに反映されます。<br>
apiに接続するときは、ヘッダ情報にアクセストークンを送信して、
DBに登録されたアクセストークンが一致かつ有効期限内であれば、
apiを使用でき、また再度Twitterにログインするときはアクセストークンを用いれば、自動的に行います。
アクセストークンをヘッダに乗せて行う際は、以下のコマンドを参考にしてください。
例)
商品全件取得をするコマンド<br>
curl http://localhost:8080/api/product -H "Authorization:アクセストークン"<br>
Webページ上はアクセストークン入力欄に書き込んでからApiを利用してください。
アクセストークンの有効期限は3分の設定です。


### 一覧画面

初めはDBに入っていないのでカラの状態です。<br>
DBに登録すると、商品のタイトルや価格の一覧が表として書き出します。<br>
表にはその商品の詳細を見ることができる画面に移動できます。<br>
検索したい商品のタイトルを入力すると、その商品の紹介のみ表に書き出されます。<br>
タイトルは完全一致出ないと書き出されません。<br>
Apiを使用するにはアクセストークン欄に有効なアクセストークンを入力した後に行わないとエラーを送信します。

### 作成画面

商品の紹介を作成する画面です。<br>
作成画面には、
 <li>商品のタイトル
 <li>商品の説明
 <li>商品の価格
 <li>商品の画像
の4項目を入力することで商品の紹介を作成することができます。<br>
作成は全ての項目を入力しなければなりません。<br>
また、アクセストークンを基に作者を追加します。
Apiを使用するにはアクセストークン欄に有効なアクセストークンを入力した後に行わないとエラーを送信します。


### 詳細画面

１つの商品の紹介・編集をする画面です。<br>
商品の詳細を書き出す部分が上に、
商品の編集をする部分が下に構成されている画面です。<br>
編集は作成と同様に全ての項目を入力しなければなりません。<br>
また、アクセストークンを基に作者を追加します。<br>
その商品のデータを消去するボタンもあります。
Apiを使用するにはアクセストークン欄に有効なアクセストークンを入力した後に行わないとエラーを送信します。


### Twitter画面

Twitterの個人情報を出力する画面です。<br>
Twitterのユーザ名を表示し、
それ以降に自分のフォロワー, タイムラインツイートとその出所についての情報を表示します。<br>
ログアウトボタンを押すことでTwitterからログアウトします。<br>
Twitterに再度ログインするときは、アクセストークンが有効であれば、
自動ログインする。<br>

## API一覧
|api名|HTTPメソッド|リソースパス|
|---|---|---|
|商品一件作成|POST|/api/product/|
|商品一件取得|GET|/api/product/{id}|
|商品一件更新|POST|/api/product/{id}|
|商品一件削除|DELETE|/api/product/{id}|
|商品全件取得|GET|/api/product/|
|商品取得|POST|/api/product/sam|

## ページ一覧
|ページ名|パス|
|---|---|
|一覧ページ|/|
|詳細ページ|/{id}|
|作成ページ|/create|
|twitterページ|/twitter|
|ログインするページ|/connect/twitter|
|ログアウトページ|/logout|
