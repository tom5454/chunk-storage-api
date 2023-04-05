package com.tom.chunkstorage.api;

import net.minecraft.nbt.CompoundTag;

public interface DataObject {
	CompoundTag save();
	void load(CompoundTag compound);
}
