# Chunk Storage Api
Chunk Storage API Fabric is a powerful library mod designed specifically for mod developers who want to store NBT data in each Minecraft chunk. This mod provides an easy-to-use API that allows developers to store and retrieve data in a lightweight and efficient manner. Similar to Forge's Chunk Capabilities, Chunk Storage API Fabric provides a similar functionality for Fabric Loader users.

## Setup
Add my maven repo:

```gradle
repositories {
  maven {
    name = "tom5454 maven"
    url = "https://raw.githubusercontent.com/tom5454/maven/main"
  }
}
```

Latest version: ![Latest version badge](https://img.shields.io/maven-metadata/v?color=forestgreen&label=release&metadataUrl=https%3A%2F%2Fraw.githubusercontent.com%2Ftom5454%2Fmaven%2Fmain%2Fcom%2Ftom5454%2Fchunkstorage%2FChunkStorageFabric-119%2Fmaven-metadata.xml)

Add it to your dependencies:

```gradle
dependencies {
  /* minecraft dependency is here */
  
  modImplementation(include("com.tom5454.chunkstorage:ChunkStorageFabric-119:${project.chunk_storage_api_version}"))
}
```

## Creating a DataObject
Create a class and implement the `com.tom.chunkstorage.api.DataObject` interface.

```java
public class MyDataObject implements DataObject {
	
	public NbtCompound save() {
		NbtCompound compound = new NbtCompound();
		//Save your object
		return compound;
	}
	
	public void load(NbtCompound compound) {
		//Load your object
	}
}
```

Then in your mod init register your data type.

```java
public static DataObjectKey<MyDataObject> MY_DATA_OBJECT_KEY;

@Override
public void onInitialize() {
	// other init code ...
	MY_DATA_OBJECT_KEY = ChunkStorageApi.registerObjectFactory(new Identifier("mymod:my_object"), MyDataObject::new);
}
```

## Accessing the Data
You can only access the data from the server side `!World.isClient()`!  

```java
WorldChunk chunk = world.getWorldChunk(blockPos);//yarn
LevelChunk chunk = level.getChunkAt(blockPos);//official

//Get the stored data or null
MyDataObject data = ChunkStorageApi.getFromChunk(chunk, MyMod.MY_DATA_OBJECT_KEY);

//Get the stored data or create new
MyDataObject data = ChunkStorageApi.getOrCreateFromChunk(chunk, MyMod.MY_DATA_OBJECT_KEY);
```

