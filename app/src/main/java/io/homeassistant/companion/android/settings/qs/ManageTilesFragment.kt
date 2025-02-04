package io.homeassistant.companion.android.settings.qs

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.accompanist.themeadapter.material.MdcTheme
import com.mikepenz.iconics.typeface.IIcon
import dagger.hilt.android.AndroidEntryPoint
import io.homeassistant.companion.android.R
import io.homeassistant.companion.android.settings.qs.views.ManageTilesView
import io.homeassistant.companion.android.util.icondialog.IconDialog
import io.homeassistant.companion.android.common.R as commonR

@AndroidEntryPoint
class ManageTilesFragment : Fragment() {

    companion object {
        private const val TAG = "TileFragment"
        val validDomains = listOf(
            "automation", "button", "cover", "fan", "humidifier", "input_boolean", "input_button", "light",
            "lock", "media_player", "remote", "siren", "scene", "script", "switch"
        )
    }

    val viewModel: ManageTilesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @Deprecated("Deprecated in Java")
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        menu.findItem(R.id.get_help)?.let {
            it.isVisible = true
            it.intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://companion.home-assistant.io/docs/integrations/android-quick-settings"))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MdcTheme {
                    var showingDialog by remember { mutableStateOf(false) }

                    if (showingDialog) {
                        IconDialog(
                            onSelect = {
                                onIconDialogIconsSelected(it)
                                showingDialog = false
                            },
                            onDismissRequest = { showingDialog = false }
                        )
                    }

                    ManageTilesView(
                        viewModel = viewModel,
                        onShowIconDialog = {
                            showingDialog = true
                        }
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.title = getString(commonR.string.tiles)
    }

    private fun onIconDialogIconsSelected(selectedIcon: IIcon) {
        Log.d(TAG, "Selected icon: ${selectedIcon.name}")
        viewModel.selectIcon(selectedIcon)
    }
}
