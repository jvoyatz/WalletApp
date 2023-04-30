package gr.jvoyatz.assignment.core.ui.custom.views.account

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import gr.jvoyatz.assignment.core.common.utils.Utils
import gr.jvoyatz.assignment.core.ui.R
import gr.jvoyatz.assignment.core.ui.databinding.AccountViewBinding
import gr.jvoyatz.assignment.core.ui.utils.fromBottomAnimation
import gr.jvoyatz.assignment.core.ui.utils.fromTopAnimation
import gr.jvoyatz.assignment.core.ui.utils.show

class AccountView(
    context: Context,
    attrs: AttributeSet?
) : ConstraintLayout(context, attrs), AccountViewInterface {

    private var isShowFavorite: Boolean = false
    private var animationType: AccountViewAnimationType = AccountViewAnimationType.NONE

    private var binding: AccountViewBinding
    init {
        init(attrs)

        binding = LayoutInflater.from(context).let {
            AccountViewBinding.inflate(it, this, true)
        }

        binding.accountFavorite.isVisible = isShowFavorite
        setAnimationType()
    }

    private fun init(attrs: AttributeSet?){
        with(context.theme.obtainStyledAttributes(attrs, R.styleable.AccountView, 0, 0)){
            try{
                isShowFavorite = getBoolean(R.styleable.AccountView_showFavorite, false)
            } finally {
                recycle()
            }
        }
    }
    override fun setAccountName(name: String?) {
        val uiName = name ?: context.getString(R.string.not_provided)
        binding.accountName.text = uiName
    }

    override fun setAccountBalance(balance: String, currency: String) {
        with("$balance ${Utils.getCurrency(currency)}"){
            binding.accountBalance.text = this
        }
    }

    override fun setAccountFavorite(favorite: Boolean) = with(binding.accountFavorite) {
        show()
        when (favorite) {
            true -> setImageResource(R.drawable.ic_favorite)
            else -> setImageResource(R.drawable.ic_favorite_border)
        }
    }

    override fun setAccountFavoriteClickListener(clickHandler: () -> Unit) {
        binding.accountFavorite.setOnClickListener {
            clickHandler()
        }
    }

    private fun setAnimationType(){
        when(animationType){
            AccountViewAnimationType.TOP -> fromTopAnimation()
            AccountViewAnimationType.BOTTOM -> fromBottomAnimation()
            else -> {}
        }
    }
    private fun setMatchParentWidth(){
        val lp = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams = lp
    }
}