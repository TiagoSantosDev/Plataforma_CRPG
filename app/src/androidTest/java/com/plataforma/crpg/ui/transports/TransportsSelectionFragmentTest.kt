package com.plataforma.crpg.ui.transports

import org.junit.Assert.assertNotNull
import org.junit.Test

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