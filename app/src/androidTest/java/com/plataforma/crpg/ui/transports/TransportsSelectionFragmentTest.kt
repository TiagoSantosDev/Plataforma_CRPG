package com.plataforma.crpg.ui.transports

import androidx.appcompat.app.AppCompatActivity
import com.plataforma.crpg.model.Transport
import kotlinx.android.synthetic.main.fragment_transport_selection.view.*
import org.junit.Test

import org.junit.Assert.*

class TransportsSelectionFragmentTest {

    var selectionFrag: TransportsSelectionFragment? = null

    @Test
    fun onCreateView() {
        assertNotNull(selectionFrag)
    }

    @Test
    fun onViewCreated() {
    }

    @Test
    fun onActivityCreated() {
        //assertNotNull(selectionFrag!!.view!!.findViewById(R.id.botao_escolha_transportes_fixos))
    }
}