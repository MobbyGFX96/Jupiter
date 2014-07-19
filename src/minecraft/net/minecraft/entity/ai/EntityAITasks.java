package net.minecraft.entity.ai;

import net.minecraft.profiler.Profiler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EntityAITasks {
    private static final Logger logger = LogManager.getLogger();
    private static final String __OBFID = "CL_00001588";
    private final Profiler theProfiler;
    private List taskEntries = new ArrayList();
    private List executingTaskEntries = new ArrayList();
    private int tickCount;
    private int tickRate = 3;

    public EntityAITasks(Profiler p_i1628_1_) {
        this.theProfiler = p_i1628_1_;
    }

    public void addTask(int p_75776_1_, EntityAIBase p_75776_2_) {
        this.taskEntries.add(new EntityAITasks.EntityAITaskEntry(p_75776_1_, p_75776_2_));
    }

    public void removeTask(EntityAIBase p_85156_1_) {
        Iterator var2 = this.taskEntries.iterator();

        while (var2.hasNext()) {
            EntityAITasks.EntityAITaskEntry var3 = (EntityAITasks.EntityAITaskEntry) var2.next();
            EntityAIBase var4 = var3.action;
            if (var4 == p_85156_1_) {
                if (this.executingTaskEntries.contains(var3)) {
                    var4.resetTask();
                    this.executingTaskEntries.remove(var3);
                }

                var2.remove();
            }
        }
    }

    public void onUpdateTasks() {
        ArrayList var1 = new ArrayList();
        Iterator var2;
        EntityAITasks.EntityAITaskEntry var3;
        if (this.tickCount++ % this.tickRate == 0) {
            var2 = this.taskEntries.iterator();

            while (var2.hasNext()) {
                var3 = (EntityAITasks.EntityAITaskEntry) var2.next();
                boolean var4 = this.executingTaskEntries.contains(var3);
                if (var4) {
                    if (this.canUse(var3) && this.canContinue(var3)) {
                        continue;
                    }

                    var3.action.resetTask();
                    this.executingTaskEntries.remove(var3);
                }

                if (this.canUse(var3) && var3.action.shouldExecute()) {
                    var1.add(var3);
                    this.executingTaskEntries.add(var3);
                }
            }
        } else {
            var2 = this.executingTaskEntries.iterator();

            while (var2.hasNext()) {
                var3 = (EntityAITasks.EntityAITaskEntry) var2.next();
                if (!var3.action.continueExecuting()) {
                    var3.action.resetTask();
                    var2.remove();
                }
            }
        }

        this.theProfiler.startSection("goalStart");
        var2 = var1.iterator();

        while (var2.hasNext()) {
            var3 = (EntityAITasks.EntityAITaskEntry) var2.next();
            this.theProfiler.startSection(var3.action.getClass().getSimpleName());
            var3.action.startExecuting();
            this.theProfiler.endSection();
        }

        this.theProfiler.endSection();
        this.theProfiler.startSection("goalTick");
        var2 = this.executingTaskEntries.iterator();

        while (var2.hasNext()) {
            var3 = (EntityAITasks.EntityAITaskEntry) var2.next();
            var3.action.updateTask();
        }

        this.theProfiler.endSection();
    }

    private boolean canContinue(EntityAITasks.EntityAITaskEntry p_75773_1_) {
        this.theProfiler.startSection("canContinue");
        boolean var2 = p_75773_1_.action.continueExecuting();
        this.theProfiler.endSection();
        return var2;
    }

    private boolean canUse(EntityAITasks.EntityAITaskEntry p_75775_1_) {
        this.theProfiler.startSection("canUse");
        Iterator var2 = this.taskEntries.iterator();

        while (var2.hasNext()) {
            EntityAITasks.EntityAITaskEntry var3 = (EntityAITasks.EntityAITaskEntry) var2.next();
            if (var3 != p_75775_1_) {
                if (p_75775_1_.priority >= var3.priority) {
                    if (this.executingTaskEntries.contains(var3) && !this.areTasksCompatible(p_75775_1_, var3)) {
                        this.theProfiler.endSection();
                        return false;
                    }
                } else if (this.executingTaskEntries.contains(var3) && !var3.action.isInterruptible()) {
                    this.theProfiler.endSection();
                    return false;
                }
            }
        }

        this.theProfiler.endSection();
        return true;
    }

    private boolean areTasksCompatible(EntityAITasks.EntityAITaskEntry p_75777_1_, EntityAITasks.EntityAITaskEntry p_75777_2_) {
        return (p_75777_1_.action.getMutexBits() & p_75777_2_.action.getMutexBits()) == 0;
    }

    class EntityAITaskEntry {
        private static final String __OBFID = "CL_00001589";
        public EntityAIBase action;
        public int priority;

        public EntityAITaskEntry(int p_i1627_2_, EntityAIBase p_i1627_3_) {
            this.priority = p_i1627_2_;
            this.action = p_i1627_3_;
        }
    }
}
