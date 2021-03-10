package pink.zak.minestom.bunkers.combat.meta.types;

public class BlockMeta {
    private final boolean climbable;

    public BlockMeta(boolean climbable) {
        this.climbable = climbable;
    }

    public boolean isClimbable() {
        return this.climbable;
    }
}
