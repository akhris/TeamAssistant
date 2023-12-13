package utils

import java.nio.file.FileSystemNotFoundException
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.pathString


object ResourcesUtils {

    fun getResources(folder: ResourcesFolder): List<String> {
        val uri = object {}::class.java.getResource(folder.path)?.toURI() ?: return listOf()
        val dirPath = try {
            Paths.get(uri)
        } catch (e: FileSystemNotFoundException) {
            // If this is thrown, then it means that we are running the JAR directly (example: not from an IDE)
            val env = mutableMapOf<String, String>()
            FileSystems.newFileSystem(uri, env).getPath(folder.path)
        }
        return Files.list(dirPath).map { f ->
                       f.pathString
        }.toList()
    }

    sealed class ResourcesFolder {
        abstract val path: String

        object ProjectIcons : ResourcesFolder() {
            override val path = "/vector/projects"
        }

        object UserIcons : ResourcesFolder() {
            override val path = "/vector/users"
        }
    }
}