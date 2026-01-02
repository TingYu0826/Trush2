請依照以下原則「只做最小必要修改」修正 PMD violation，
不要重構架構、不要影響既有測試、不要改動 production logic 行為。

【整體原則】
1. 只處理「低風險、好說明」的 PMD violation。
2. 所有修改都要在 DEVLOG.md 紀錄「為何修、修了什麼、不修什麼」。
3. 絕對不可為了 PMD 覆蓋度而新增不合理測試或改動業務邏輯。

--------------------------------------------------
【第一優先：Documentation 類（最安全、最好改）】

請修正以下類型：
- CommentRequired
- ClassCommentRequired
- PublicMethodCommentRequired

修改方式：
- 為 public class、public method 補「簡短 JavaDoc」
- JavaDoc 只需一句描述用途即可，不需詳細說明

請處理目標 package：
- edu.fcu.service
- edu.fcu.strategy
- edu.fcu.controller

❌ 不需處理：
- private method
- test code

--------------------------------------------------
【第二優先：Unused / Redundant 類（低風險）】

請修正：
- UnusedImports
- UnusedPrivateField
- UnusedLocalVariable
- RedundantFieldInitializer

修改方式：
- 移除未使用 import
- 刪除完全未使用的 private 欄位
- 不可影響 constructor 或對外 API

--------------------------------------------------
【第三優先：可安全忽略但需說明的 violation（不改 code）】

以下 violation 請「不要修改程式碼」，僅在 DEVLOG.md 說明原因：
- CyclomaticComplexity
- GodClass
- TooManyMethods
- default: throw new IllegalArgumentException("Unsupported vehicle type")

DEVLOG 說明方向：
- Dispatcher/Strategy 屬於刻意集中邏輯以提高測試複雜度
- default 分支屬防禦性程式碼，依系統設計為 unreachable

--------------------------------------------------
【禁止事項（非常重要）】

❌ 不可：
- 改變任何 if/else 條件
- 改變派車行為
- 為了 PMD 新增假邏輯
- 修改 enum 或 production 規範

--------------------------------------------------
【輸出要求】

1. 修改完成後，請列出：
    - 修正了哪些 violation 類型
    - 哪些 violation 明確選擇不修（含理由）
2. 同步更新 DEVLOG.md（用條列式）
