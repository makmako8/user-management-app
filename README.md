# ユーザー管理アプリ（user-management-app）

## 📌 概要
Spring Bootを用いたWebアプリケーションで、ユーザー情報の登録・編集・削除や、タスク管理・CSV出力機能を実装しています。

## 🔧 主な機能
- ログイン・ログアウト機能（Spring Security）
- 一般ユーザー / 管理者のロール分離
- ユーザー情報のCRUD（登録・更新・削除・一覧）
- タスク管理（登録・完了トグル・CSV出力）
- CSV出力（ユーザー/タスクごと）
- 検索フィルター（名前検索など）
- バリデーションエラーメッセージ表示

## 🛠 使用技術
- Java 17  
- Spring Boot 3.x  
- Spring Security  
- Thymeleaf  
- JPA (Hibernate)  
- MySQL または H2 (設定で切り替え可)  
- Bootstrap (CSSスタイル用)

## 📷 画面キャプチャ（任意）
※`screenshots/`フォルダに保存してREADMEに貼ると良いです。

## ▶️ 起動方法
1. このリポジトリをクローン
2. `application.properties` にDB設定を記入
3. `mvn spring-boot:run`で起動
4. ブラウザで `http://localhost:8080` にアクセス

## 🙋‍♀️ 開発者
- 駒田磨衣子（Makmako8）
