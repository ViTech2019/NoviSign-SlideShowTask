package com.vito.novisignslideshowtask

import android.util.Log
import com.vito.novisignslideshowtask.data.model.Playlist
import com.vito.novisignslideshowtask.data.model.PlaylistItem
import com.vito.novisignslideshowtask.data.model.PlaylistResponse
import com.vito.novisignslideshowtask.data.remote.NoviSignApi
import com.vito.novisignslideshowtask.data.repo.PlaylistRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class PlaylistRepositoryTest {

    // fake mock api
    private val api = mockk<NoviSignApi>()

    //repository instance to test
    private val repository = PlaylistRepository(api)

    // mock Log.d to avoid  test crash
    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
    }

    // test for getting playlist data
    @Test
    fun `fetchPlaylist returns playlist when API succeeds`() = runTest {
        // fake playlist data
        val playlistResponse = PlaylistResponse(
            screenKey = "testScreen",
            company = "testCompany",
            breakpointInterval = 0,
            modified = 42L,
            playlists = listOf(
                Playlist(
                    channelTime = 0,
                    playlistKey = "testPlaylist",
                    playlistItems = listOf(
                        PlaylistItem(
                            creativeKey = "file123.jpg",
                            creativeLabel = "Test Image",
                            duration = 10
                        )
                    )
                )
            )
        )

        // return the fake playlist when the api is called
        coEvery { api.getPlaylist(any()) } returns playlistResponse

        // server call repository function
        val result = repository.fetchPlaylist("testScreen")

        //check that the returned data is correct
        assertEquals("testScreen", result.screenKey)
        assertEquals(42L, result.modified)
        assertEquals(1, result.playlists.first().playlistItems.size)
        assertEquals("file123.jpg", result.playlists.first().playlistItems.first().creativeKey)
    }

    // test for downloading media files
    @Test
    fun `downloadMediaFile saves file with correct content`() = runTest {
        // prepare fake response
        val testBytes = "Hello World Buddy".toByteArray()
        val responseBody = testBytes.toResponseBody("application/octet-stream".toMediaType())

        // return the fake response
        coEvery { api.getMediaFile(any()) } returns Response.success(responseBody)

        // call the download-file function
        val file = repository.downloadMediaFile("file12354.jpg")

        // check that the file was created and has the correct bytes
        assertTrue(file.exists())
        val content = file.readBytes()
        assertArrayEquals(testBytes, content)
    }

    // test for handling failed download
    @Test(expected = Exception::class)
    fun `downloadMediaFile throws exception when API fails`() = runTest {
        // prepare a failed response - 404
        val errorBody = "Not Found".toByteArray().toResponseBody("text/plain".toMediaType())
        coEvery { api.getMediaFile(any()) } returns Response.error(404, errorBody)

        // this should throw an exception
        repository.downloadMediaFile("file_not_exist.jpg")
    }
}
