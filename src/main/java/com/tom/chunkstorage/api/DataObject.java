package com.tom.chunkstorage.api;

import net.minecraft.nbt.CompoundTag;

/**
 * Implement this interface on a class to store, and register it using {@link ChunkStorageApi#registerObjectFactory(net.minecraft.resources.ResourceLocation, java.util.function.Supplier)}
 *
 * @author tom5454
 * */
public interface DataObject {

	/**
	 * Store the object into NBT
	 *
	 * @return NBT Compound
	 * */
	CompoundTag save();

	/**
	 * Load the object from NBT
	 *
	 * @param compound
	 * */
	void load(CompoundTag compound);
}
