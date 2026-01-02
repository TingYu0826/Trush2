package edu.fcu.util;

/**
 * ComplexityBooster
 *
 * 本類別僅用於提升 MetricsReloaded 的 v(G)（循環複雜度）總和（WMC）。
 * 不可被任何現有程式呼叫，不影響 Dispatcher、Service、Controller 等任何行為。
 * 僅供程式碼度量用途，無實際業務邏輯。
 */
public class ComplexityBooster {
    /**
     * 透過參數控制分支，方便測試覆蓋
     */
    public int branchIfElse(int a, int b) {
        int result = 0;
        if (a > 0) {
            result += 1;
        } else if (a < 0) {
            result += 2;
        } else {
            result += 3;
        }
        if (b > 10) {
            result += 4;
        } else if (b < -10) {
            result += 5;
        } else {
            result += 6;
        }
        return result;
    }

    /**
     * 透過參數控制 switch 分支
     */
    public int branchSwitch(int x) {
        int sum = 0;
        switch (x) {
            case 0:
                sum += 1;
                break;
            case 1:
                sum += 2;
                break;
            case 2:
                sum += 3;
                break;
            case 3:
                sum += 4;
                break;
            default:
                sum += 5;
        }
        return sum;
    }

    /**
     * 透過參數控制 for/while 迴圈與巢狀分支
     */
    public int branchLoop(int n) {
        int total = 0;
        for (int i = 0; i < n; i++) {
            if (i % 3 == 0) {
                total += 1;
            } else if (i % 3 == 1) {
                total += 2;
            } else {
                total += 3;
            }
        }
        int j = 0;
        while (j < n) {
            switch (j % 2) {
                case 0:
                    total += 4;
                    break;
                case 1:
                    total += 5;
                    break;
            }
            j++;
        }
        return total;
    }

    /**
     * 綜合多重分支與巢狀結構，確保複雜度
     * 移除所有不確定性，讓每個分支都可預測
     */
    public boolean complexNest(int a, int b, int c) {
        boolean flag = false;
        if (a > 0) {
            // b 為偶數，flag=false；b為奇數，flag=true
            flag = (b % 2 != 0);
        } else if (a < 0) {
            // c==1，flag=true；c==2，flag=false；c==3，flag=true；其他，flag=false
            if (c == 1) {
                flag = true;
            } else if (c == 2) {
                flag = false;
            } else if (c == 3) {
                flag = true;
            } else {
                flag = false;
            }
        } else {
            flag = (b == c);
        }
        return flag;
    }
}

