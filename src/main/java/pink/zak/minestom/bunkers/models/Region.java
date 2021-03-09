package pink.zak.minestom.bunkers.models;

import net.minestom.server.MinecraftServer;
import net.minestom.server.chat.ColoredText;
import net.minestom.server.utils.BlockPosition;
import net.minestom.server.utils.Position;
import org.jetbrains.annotations.NotNull;

public class Region {
    private final int minX;
    private final int maxX;
    private final int minZ;
    private final int maxZ;

    public Region(BlockPosition cornerOne, BlockPosition cornerTwo) {
        this(
                Math.min(cornerOne.getX(), cornerTwo.getX()),
                Math.max(cornerOne.getX(), cornerTwo.getX()),
                Math.min(cornerOne.getZ(), cornerTwo.getZ()),
                Math.max(cornerOne.getZ(), cornerTwo.getZ())
        );
    }

    public Region(int minX, int maxX, int minZ, int maxZ) {
        this.minX = minX;
        this.maxX = maxX;
        this.minZ = minZ;
        this.maxZ = maxZ;
    }

    public int getMinX() {
        return this.minX;
    }

    public int getMaxX() {
        return this.maxX;
    }

    public int getMinZ() {
        return this.minZ;
    }

    public int getMaxZ() {
        return this.maxZ;
    }

    public boolean isPositionWithin(@NotNull Position position) {
        return position.getX() > this.minX && position.getX() < this.maxX && position.getZ() > this.minZ && position.getZ() < this.maxZ;
    }
}
