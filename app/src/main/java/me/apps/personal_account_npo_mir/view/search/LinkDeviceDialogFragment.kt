package me.apps.personal_account_npo_mir.view.search

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.google.android.material.button.MaterialButton
import me.apps.personalaccountnpomir.R

class LinkDeviceDialogFragment : DialogFragment() {

    interface Listener {
        fun onPasswordEntered(meterId: Int, password: String)
    }

    private var listener: Listener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        listener = when {
            parentFragment is Listener -> parentFragment as Listener
            context is Listener -> context
            else -> null
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.88).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.dialog_link_device, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val meterId = requireArguments().getInt(ARG_METER_ID)
        val deviceName = requireArguments().getString(ARG_DEVICE_NAME).orEmpty()

        val serialNumberTitleTextView =
            view.findViewById<TextView>(R.id.serialNumberTitleTextView)
        val passwordEditText =
            view.findViewById<EditText>(R.id.passwordEditText)
        val togglePasswordButton =
            view.findViewById<ImageButton>(R.id.togglePasswordButton)
        val cancelButton =
            view.findViewById<MaterialButton>(R.id.cancelButton)
        val okButton =
            view.findViewById<MaterialButton>(R.id.okButton)

        serialNumberTitleTextView.text = deviceName

        var passwordVisible = false

        togglePasswordButton.setOnClickListener {
            passwordVisible = !passwordVisible

            if (passwordVisible) {
                passwordEditText.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                togglePasswordButton.setImageResource(R.drawable.ic_eye_off)
            } else {
                passwordEditText.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                togglePasswordButton.setImageResource(R.drawable.ic_eye)
            }

            passwordEditText.setSelection(passwordEditText.text.length)
        }

        cancelButton.setOnClickListener {
            dismiss()
        }

        okButton.setOnClickListener {
            val password = passwordEditText.text.toString().trim()
            listener?.onPasswordEntered(meterId, password)
            dismiss()
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        private const val ARG_METER_ID = "ARG_METER_ID"
        private const val ARG_DEVICE_NAME = "ARG_DEVICE_NAME"

        fun newInstance(meterId: Int, deviceName: String): LinkDeviceDialogFragment {
            val fragment = LinkDeviceDialogFragment()
            fragment.arguments = Bundle().apply {
                putInt(ARG_METER_ID, meterId)
                putString(ARG_DEVICE_NAME, deviceName)
            }
            return fragment
        }
    }
}