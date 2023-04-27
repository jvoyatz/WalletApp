package gr.jvoyatz.assignment.core.navigation

import androidx.fragment.app.Fragment
import javax.inject.Inject

class NavigatorProvider @Inject constructor(
    var fragmentNavigator: Navigator.FragmentNavigator
) {
    fun getNavigator(fragment: Fragment):Navigator{
        return fragmentNavigator.apply {
            this.fragment = fragment
        }
    }
}