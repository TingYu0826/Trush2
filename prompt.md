請修正派車分流邏輯，使其符合「政府清潔隊大型家具回收實務」，規則如下：

【核心規則（務必遵守）】
1. 抓斗車（Grab Truck）「唯一」派遣條件：
  - item.damaged == true
  - 代表破損、可壓縮、不需完整搬運

2. 只要 item.damaged == false：
  - 一律走平板車（Flatbed Truck）
  - 不論尺寸大小、類別名稱、體積

【禁止行為】
- 不可依據尺寸（長寬高）判斷是否使用抓斗車
- 不可依據類別（床墊 / 沙發 / 櫃子）判斷是否使用抓斗車
- 抓斗車不得處理「未破損家具」

【實作要求】
- Dispatcher 中：
  - 先將 items 分為 damagedItems 與 intactItems
  - damagedItems → GrabTruckLoadCheckStrategy（points 模型）
  - intactItems → FlatbedLoadCheckStrategy（條帶法＋裝載率）

- GrabTruckLoadCheckStrategy：
  - 僅接受 damaged == true 的 item
  - 若 damaged == false，必須拒絕

- 平板車邏輯不得修改（維持現有單層條帶法）

【驗證案例（必須通過）】
- 雙人床墊（damaged=false）→ 平板車
- 三人沙發（damaged=false）→ 平板車
- 破損衣櫃（damaged=true）→ 抓斗車
- 多件破損家具 → 抓斗車 points 分批換車

請同步修正後端與前端顯示，確保：
- 抓斗車只顯示 points
- 平板車才顯示裝載率
