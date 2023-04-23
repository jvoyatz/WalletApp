package gr.jvoyatz.afse.core.navigation

import androidx.fragment.app.Fragment
 sealed interface Navigator {
     fun navigate(destination: Destination)

     abstract class FragmentNavigator : Navigator{
         var fragment: Fragment? = null
     }
 }


