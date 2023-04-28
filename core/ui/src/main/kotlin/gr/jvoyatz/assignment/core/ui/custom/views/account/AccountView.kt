package gr.jvoyatz.assignment.core.ui.custom.views.account

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import gr.jvoyatz.assignment.core.common.utils.Utils
import gr.jvoyatz.assignment.core.ui.R
import gr.jvoyatz.assignment.core.ui.databinding.LayoutAccountsItemBinding
import gr.jvoyatz.assignment.core.ui.utils.fromBottomAnimation

class AccountView(
    context: Context,
    attrs: AttributeSet?
) : ConstraintLayout(context, attrs), AccountViewInterface {

    private var isShowFavorite: Boolean = false

    private var binding: LayoutAccountsItemBinding
    init {
        obtainAttributes(attrs)

        binding = LayoutInflater.from(context).let {
            LayoutAccountsItemBinding.inflate(it, this, true)
        }
        setMatchParentWidth()
        binding.accountFavorite.isVisible = isShowFavorite

        fromBottomAnimation()
    }

    private fun setMatchParentWidth(){
        val lp = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams = lp
    }

    private fun obtainAttributes(attrs: AttributeSet?){
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
}