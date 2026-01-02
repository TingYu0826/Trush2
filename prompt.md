你現在是我的軟體測試課程期末專題助教 + 工程師。請你先完整掃描整個 IntelliJ 專案（Java + Spring Boot + 前端靜態頁面），確認目前程式結構與派車核心邏輯（Dispatcher / strategy / util / model / controller）後，依照以下「交付規格」進行實作與產出。

========================
【A. 作業規格與硬性指標】
========================
本專題需達成：
1) 有意義的功能 > 5 個（需能在報告中列點說明）
2) WMC > 200（v(G) 總和，MetricsReloaded 會檢測）
3) 單元測試總數量 >= 50（Maven Surefire 統計）
4) Branch coverage >= 90%（Jacoco）
5) Bug & Fix >= 10（需整理可寫進報告的缺陷與修正紀錄）
   同時要有：
- 測試規劃 / 測試設計（等價類、邊界值、pairwise 等）
- 自動化測試 JUnit
- PMD report
- Jacoco report
- 程式碼度量報告（MetricsReloaded 的指標需能說明）

【重要限制】
- 不允許「灌水式」測試（例如只測 getter/setter 或 assertTrue(true) 這種）
- 測試要能對應到明確的「測試設計方法」與「需求/規格」
- 允許使用 CSV 進行資料驅動測試（Data-driven testing）
- 允許必要的小幅重構以提升可測性，但不得破壞現有功能與前端顯示

========================
【B. 你要先做的診斷（必做）】
========================
1) 列出目前專案的「有意義功能」清單至少 6 個（之後報告直接可用）
   例如（請依實際程式確認後修正文字）：
  - 物品輸入驗證（長寬高不得為負、不得為 0、類別不得空）
  - 破損/可壓縮(damaged) 的分類邏輯 → 抓斗車
  - 未破損的大型家具 → 平板車條帶法裝載
  - 平板車支援旋轉擺放（OrientationHelper）
  - 平板車滿載時自動新增下一台車（多車派遣）
  - 抓斗車以 points 分批派遣（每台上限 100 點）
  - 無法派車時回傳錯誤訊息並在前端顯示（若有）
2) 找出最適合寫單元測試的模組（優先順序）：
  - service/Dispatcher
  - strategy/*LoadCheckStrategy
  - util/*（orientation / packing / fit check）
3) 找出目前最可能的 bug/風險點（至少 10 個候選），之後會變成 Bug & Fix 清單

========================
【C. 測試方法（必須全部出現在報告 & 程式中）】
========================
你要同時採用以下測試方法，並在程式中落實：

(1) 等價類劃分（Equivalence Partitioning）
- 輸入維度：length/width/height/category/damaged
- 例如 length: {<=0, (0~合理上限], >合理上限}
- damaged: {true,false}
- category: {空字串, 一般家具, 冰箱類, 沙發/床墊, 金屬類}
  請你定義「合理上限」與「不合理上限」的依據（用註解說明即可，例如超過 10m 視為異常資料）

(2) 邊界值分析（Boundary Value Analysis）
必測邊界：
- 抓斗車 points：99 / 100 / 101（應決定是否換下一台）
- 平板車 usableWidth/usableLength 的臨界：剛好能放 vs 差 0.01 放不下
- 尺寸 0.0、極小值、極大值（例如 0.01、10.0）
- 旋轉後剛好能放的案例（OrientationHelper 的價值）

(3) Pairwise / All-Pairs（全成對）
- 用 pairwise 產生至少 15~25 筆組合測試資料
- 欄位包含：damaged(2) × category(至少3) × sizeLevel(至少3) × shapeType(至少2)
- 這些資料放在 CSV，JUnit 參數化測試讀取

(4) 白箱（White-box）分支覆蓋導向測試
- 目標：Jacoco branch coverage >= 90%
- 你要根據現有 if/else/loop/early return 來補足測試
- 每個 strategy 與 Dispatcher 的每個分支都要被觸發（成功/失敗/空輸入/超量/換車/旋轉成功/旋轉失敗）

(5) 負向測試（Negative testing）+ 例外處理測試
- null / empty list / category null / NaN / Infinity（若系統可能收到）
- 不合法尺寸（負值、0）
- 無法派車的案例要有明確回傳（或丟出特定例外），並被測試驗證

========================
【D. 測試實作要求（你要真的寫出來）】
========================
1) 建立 src/test/java 下的測試結構，package 對齊 edu.fcu.*
2) 使用 JUnit 5 + AssertJ（若專案未引入 AssertJ，可用 JUnit assertions）
3) 至少要有這些測試類別（名稱可調，但目的要一致）：
  - DispatcherTest（核心：分流 + 多車派遣 + fallback）
  - GrabTruckPointCalculatorTest（points 邏輯）
  - GrabTruckLoadCheckStrategyTest（依 points 分批、上限100、空輸入）
  - FlatbedLoadCheckStrategyTest（條帶法、旋轉、放不下、換車）
  - OrientationHelperTest（6 種旋轉是否完整且不重複、可用於 fitting）
  - ValidationTest 或 ItemFactoryTest（不合法輸入處理）
  - LoadControllerWebMvcTest（少量：API 回傳格式與 http 狀態）
4) 測試數量 >= 50：
  - 不要寫 50 個無意義測試
  - 請用 ParameterizedTest + CSV 產生大量有效案例
  - 每個測試要有清楚的 DisplayName 與註解，對應測試設計方法
5) CSV 測試資料：
  - 放在 src/test/resources/
  - 至少三份：
    a) equivalence_cases.csv（等價類）
    b) boundary_cases.csv（邊界）
    c) pairwise_cases.csv（全成對）
  - CSV 欄位至少包含：name,length,width,height,category,damaged,expectedVehicle,expectedTruckCountOrPoints(optional)
6) 測試要驗證「分類正確」：
  - damaged==true 必須走抓斗車
  - damaged==false 常見家具（床墊、沙發、衣櫃）應優先平板車
  - 針對冰箱：請根據現行需求定義規則（例如：冰箱若 damaged==true -> 抓斗車；若 damaged==false 仍可平板車但 points 計算不同 / 或仍抓斗車），規則要一致、可測、可在報告說明
  - 如果目前 Dispatcher 分流規則不合理，請小幅重構（保留前端相容）並補測試
7) coverage 提升策略：
  - 針對每個 if/else 都準備「觸發此分支」的測資
  - 針對 while/for loop 準備「0 次、1 次、多次」情境
  - 針對 early return 準備「觸發」與「不觸發」情境

========================
【E. Maven / 報告產出（你要幫我補好）】
========================
1) pom.xml：
  - 確認 surefire 設定可產生測試數量
  - 加入 jacoco plugin（prepare-agent + report）
  - 加入 pmd plugin 產生 report（若已存在就調整）
2) 產出指令（寫進 README.md / report notes）：
  - mvn test
  - mvn jacoco:report
  - mvn pmd:pmd
3) 請在專案根目錄新增 docs/TEST_PLAN.md（給我報告直接貼）
   內容要包含：
  - 測試目標與範圍（unit/component/web layer）
  - 測試方法（等價類/邊界/pairwise/白箱）
  - 測試資料設計（CSV 欄位與來源）
  - 覆蓋率策略與結果解讀方式（branch coverage）
  - 風險與限制
4) 請新增 docs/BUG_FIX_LOG.md：
  - 至少 10 條
  - 每條包含：Bug描述、重現步驟、原因分析、修正方式、影響範圍、對應測試（test 名稱）
  - 若你在重構中真的修到 bug，請把它寫成 log；不要硬掰不存在的 bug
5) 若你為了可測性做了重構：
  - 必須保持功能一致
  - 必須補對應測試
  - 必須在 BUG_FIX_LOG 或 TEST_PLAN 說明

========================
【F. 最終驗收清單（你要自己檢查完再回報）】
========================
你完成後請回報我：
1) 測試總數（Surefire 顯示）
2) Jacoco branch coverage（%）
3) PMD report 是否成功產生
4) 你列出的 6+ 有意義功能清單
5) 你新增的測試檔案列表（路徑）
6) CSV 檔案列表（路徑）
7) BUG_FIX_LOG 的 10 條摘要
8) 若仍有未達標的地方，請明確指出原因與下一步建議（不可含糊）

開始執行。先做【B 診斷】→ 再做【D 測試】→ 再做【E 報告檔】→ 最後做【F 回報】。



【硬性規則：自動建立/維護變更日誌（必做）】
從現在開始，你對此專案做的「任何」修改（包含新增/刪除/重構/調參/pom.xml/前端/測試/CSV/註解/README），都必須同步更新一份 Markdown 變更日誌，讓我之後可以直接貼到 Notion。

1) 檔案位置與名稱
- 請在專案根目錄建立：docs/DEVLOG.md
- 若不存在就建立，之後每次修改都要 append（追加），不可覆寫舊內容。

2) 記錄粒度（鉅細靡遺）
   每一個 commit 級別的修改都要記錄，粒度至少到：
- 修改了哪些檔案（完整路徑）
- 每個檔案改了什麼（重點行為/邏輯）
- 變更原因（Bug/需求/可測性/覆蓋率/PMD 等）
- 影響範圍（哪些功能可能受影響）
- 對應測試（新增/更新了哪些測試案例，測到哪些分支）
- 如何驗證（我可以執行哪些指令或步驟重現/驗證）

3) 必須包含 diff（或行號）
- 每次修改都要附「最小必要 diff」(unified diff)，或至少列出：檔名 + 修改區塊的起訖行號。
- 若 diff 太長，請用「摘要 + 關鍵 diff」並保留完整檔案清單與行號。

4) 固定格式（必須照抄）
   每次追加一筆變更，請用以下模板，並填滿每個欄位：

## [YYYY-MM-DD HH:mm] 變更標題（用一句話說改了什麼）
### 需求/動機
- ...
### 變更內容摘要
- ...
### 影響檔案清單
- `path/to/fileA`：...
- `path/to/fileB`：...
### 重要 diff（或行號）
```diff
(貼 diff)


每次回報「已完成」時，請同時貼出你剛追加到 docs/DEVLOG.md 的那一段內容，否則不要說完成。
