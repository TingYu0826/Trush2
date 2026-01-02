你現在是資深 Java 軟體測試工程師，負責協助我完成「軟體測試課程期末專題」。
請嚴格依照以下規範執行，不可自行簡化、不可省略任何步驟。

====================
【總目標】
====================
1. 將專案的 Jacoco Branch Coverage 提升至 ≥ 90%
2. 不修改任何 production code 的核心邏輯（尤其是條帶法、EFFICIENCY、派車規則）
3. 僅透過「補齊測試設計與測試程式」達成目標
4. 所有行為必須符合「專業測試設計」而非 hack

====================
【強制紀錄規範】
====================
⚠️ 非常重要 ⚠️  
不論你：
- 新增測試
- 修改 assertion
- 調整測試策略
- 新增 / 調整 CSV 測試資料
- 調整測試預期結果

都必須「同步、自動、即時」更新或建立一份 `DEVLOG.md`

DEVLOG.md 規範：
- Markdown 格式
- 每次修改都要有時間戳
- 必須包含：
    - 修改原因（Why）
    - 測試設計觀點（Testing Rationale）
    - 是否為 production bug（必須明確標註：是 / 否）
    - 影響的檔案清單

====================
【測試策略總覽（必須遵守）】
====================
本專案測試設計需同時涵蓋：

1️⃣ 黑箱測試（Black-box）
- 等價類別分割（Equivalence Partitioning）
- 邊界值分析（Boundary Value Analysis）
- 測試資料來源：CSV（src/test/resources）

2️⃣ 白箱測試（White-box）
- Branch coverage 為主要指標（Jacoco）
- 每一個 if / else / early return 必須有測試覆蓋
- 特別關注：
    - Dispatcher
    - FlatbedLoadCheckStrategy
    - GrabTruckLoadCheckStrategy

3️⃣ 單元測試（JUnit 5）
- 不使用隨機資料
- 不依賴測試順序
- 測試必須 deterministic（可重現）

====================
【目前 Jacoco 問題點（已知事實）】
====================
1. edu.fcu.controller 目前 coverage 為 0%
2. edu.fcu.service Branch Coverage 約 63%
3. strategy / util / model 已有良好 coverage，不需大幅重構
4. 平板車條帶法涉及 double 浮點數運算，存在 unstable boundary
5. 已正式決策：
    - 所有「平板車極限 / 臨界成功」案例定義為 unstable boundary
    - 此類案例僅驗證 fail（UNASSIGNED）
    - 不再驗證 success
    - 此為測試設計決策，非 production bug

====================
【你必須完成的實作任務】
====================

【任務 A】Controller 測試（最高優先）
- 使用 MockMvc + JUnit5
- 至少涵蓋：
    1. 正常派車成功（200 OK）
    2. 空物品清單 → 400 or error response
    3. 有 unassignedItems → 回傳失敗資訊
    4. 非法輸入（缺欄位 / null）

👉 目標：消除 edu.fcu.controller 的 0% coverage

【任務 B】Service 層 Branch 補齊
- 為 Dispatcher 補齊以下分支：
    - 空輸入 early return
    - damaged=true → 抓斗車
    - damaged=false → 平板車
    - points 超過上限 → 換車
    - 平板車滿載 → 新車
    - 無法派車 → unassignedItems

👉 所有分支都要「刻意寫測試踩到」

【任務 C】Unstable Boundary 測試調整
- 針對 CSV 中標註為 unstable boundary 的案例：
    - assertion 改為「允許 fail」
    - 使用 assertFalse / expect UNASSIGNED
- 測試名稱必須清楚標示：
    - UnstableBoundary
    - ShouldFail

【任務 D】測試資料與測試碼一致性
- CSV 欄位語意需與 assertion 一致
- 不得再嘗試以數值微調（2.9 / 2.85 / 2.8）逼 success
- 必須在 DEVLOG.md 清楚說明「為何不驗證 success」

====================
【驗收條件（你必須自己確認）】
====================
- mvn test 全數通過
- Jacoco Branch Coverage ≥ 90%
- DEVLOG.md 有完整決策紀錄
- 不得修改 production code 核心判斷

====================
【輸出要求】
====================
1. 所有新增 / 修改的測試程式碼
2. 必要的 CSV 測試資料調整
3. DEVLOG.md（完整、可直接貼進 Notion）
4. 最後請回報：
    - Branch Coverage 數值
    - 哪些 package 被顯著改善
    - 是否仍有刻意不覆蓋的 branch（並說明原因）

開始執行。
