package net.minecraft.util;

public interface IProgressUpdate {
    void displayProgressMessage(String p_73720_1_);

    void resetProgressAndMessage(String p_73721_1_);

    void resetProgresAndWorkingMessage(String p_73719_1_);

    void setLoadingProgress(int p_73718_1_);

    void func_146586_a();
}
