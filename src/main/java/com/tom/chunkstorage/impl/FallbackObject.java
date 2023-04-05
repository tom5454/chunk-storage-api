package com.tom.chunkstorage.impl;

import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import com.tom.chunkstorage.api.DataObject;
import com.tom.chunkstorage.api.DataObjectKey;

public class FallbackObject implements DataObject {
	private static final Supplier<FallbackObject> FACTORY = FallbackObject::new;
	private static final Function<ResourceLocation, DataObjectKey<FallbackObject>> CREATE_KEY = Util.memoize(k -> new DataObjectKey<>(k, FACTORY));
	private CompoundTag compound;

	@Override
	public CompoundTag save() {
		return compound;
	}

	@Override
	public void load(CompoundTag compound) {
		this.compound = compound;
	}

	public static DataObjectKey<?> key(ResourceLocation rl) {
		return CREATE_KEY.apply(rl);
	}
}
