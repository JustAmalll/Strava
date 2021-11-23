package com.skillbox.strava.ui.fragment.contact

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.skillbox.core.extensions.gone
import com.skillbox.core.extensions.show
import com.skillbox.core.platform.ViewBindingFragment
import com.skillbox.shared_model.contact.Contact
import com.skillbox.strava.R
import com.skillbox.strava.databinding.FragmentContactBinding
import com.skillbox.strava.ui.fragment.contact.adapter.ContactAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactFragment :
    ViewBindingFragment<FragmentContactBinding>(FragmentContactBinding::inflate) {

    override val screenViewModel by viewModels<ContactViewModel>()
    override var setLogout = true
    override val setToolbar = true
    override var resToolbarId = R.string.contact_title_app

    private val args: ContactFragmentArgs by navArgs()

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>

    private val contactAdapter by lazy {
        ContactAdapter(onItemClick = { contact ->
            openContactDetailInfo(contact)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (hasReadWithWritePermission().not()) {
            initPermissionResultListener()
            binding.contactAllow.show()
            binding.contactPermissionTitle.show()
            binding.contactAllow.setOnClickListener {
                requestReadWithWritePermissions()
            }
        } else screenViewModel.getContacts(requireContext())

        bind()
        subscribe()
    }

    private fun bind() {
        binding.contactRecycler.apply {
            adapter = contactAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun subscribe() {
        screenViewModel.contactObserver.observe(viewLifecycleOwner, { items ->
            items?.let { contactAdapter.items = it }
        })
        screenViewModel.loadDataObserver.observe(viewLifecycleOwner, { isLoad ->
            isLoad?.let {
                if (isLoad) {
                    binding.contactRecycler.gone()
                    binding.contactLoader.show()
                } else {
                    binding.contactRecycler.show()
                    binding.contactLoader.gone()
                }
            }
        })
    }

    @SuppressLint("LogNotTimber")
    private fun openContactDetailInfo(contact: Contact) {
        val intentSms = screenViewModel.getIntentContact(contact.numbers, args.userId)
        if (intentSms.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intentSms)
        } else {
            Log.e(
                "ContactFragment",
                "Не могу обработать открытие activity, не найден sms application for phone"
            )
        }
    }

    private fun hasReadWithWritePermission() =
        PERMISSIONS.all {
            ActivityCompat.checkSelfPermission(
                requireContext(), it
            ) == PackageManager.PERMISSION_GRANTED
        }

    private fun requestReadWithWritePermissions() {
        requestPermissionLauncher.launch(PERMISSIONS.toTypedArray())
    }

    private fun initPermissionResultListener() {
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissionToGrantedMap: Map<String, Boolean> ->
            permissionToGrantedMap.values.all {
                if (it) {
                    binding.contactAllow.gone()
                    binding.contactPermissionTitle.gone()
                    screenViewModel.getContacts(requireContext())
                }
                it
            }
        }
    }

    companion object {
        private val PERMISSIONS = listOfNotNull(
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS
        )
    }
}