/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mdp_preprocess_mapeotiposmpp;

import com.sgt.docmgmt.mdp.core.datamodel.DocMsg;
import com.sgt.docmgmt.mdp.core.process.CorePreProcess;
/**
 *
 * @author x262252
 *  
 * Originalmente denominada MapeoTiposMpp.java
 */
public class MDP_Preprocess_MapeoTiposMpp extends CorePreProcess{

   @Override
   public boolean ProcessMsgXML(final String ConfFunc, final String ConfTec, final DocMsg docmsg) {
        boolean ProducidoError = false;
        try {
            if (this.isLogDebug()) {
                this.LogDebug("addMultivalor.ConfFunc" + ConfFunc);
                this.LogDebug("probarprobar"+ ConfFunc);
                this.LogDebug("addMultivalor.ConfTec" + ConfTec);
                this.LogDebug("addMultivalor.MsgId" + docmsg.getDocMsgId());
                this.LogDebug("addMultivalor.XML" + docmsg.getXmlMsg());
                this.LogDebug("tipo de documento" + docmsg.getDocType());
             
                
            }
        String docType = docmsg.getDocType();
        if (docType.equalsIgnoreCase("CON_SPP_LOAN") || docType.equalsIgnoreCase("CON_SPP_PPI") || docType.equalsIgnoreCase("CON_SPP_CR_CARD") || docType.equalsIgnoreCase("CON_SPP_HYBRID") || docType.equalsIgnoreCase("LET_SPP_CON") || docType.equalsIgnoreCase("LET_SPP_CR_CARD"))
            {
            this.LogDebug("dentro " + docmsg.getDocType());
            String xmlMsg = docmsg.getXmlMsg();
            
            //declaramos constantes generales
            String xmlFimultiValor = "</valor></multivalueItem>";
            String contenidoUno = "\"string\">";
            String contenidoDos = "</Item>";
            
            //declaramos constantes persona
            String persona = "<Item name=\"G_COD_CLIENTE\" type=\"string\">";
            String xmlFipersona = "<multivalueItem name=\"G_COD_CLIENTE\" type=\"string\"><valor>";
            
            //obtenemos la posicion donde comienza el tag subtipo
            int posPer = xmlMsg.indexOf(persona);
            
            //obtenemos primera parte
            String xmlPrimero = xmlMsg.substring(0,posPer);
            
            //obtenemos el valor de persona, obteniendo cordenadas donde empieza y acaba
            int posValUnoPer = xmlMsg.indexOf(contenidoUno,posPer+9);
            int posValDosPer = xmlMsg.indexOf(contenidoDos,posValUnoPer);
            String xmlContenidoPer = xmlMsg.substring(posValUnoPer+9,posValDosPer);
            
            //ultima parte del xml
            String xmlFiUltimo = xmlMsg.substring(posValDosPer+7,xmlMsg.length());
            
            String xmlFinalMsg = xmlPrimero.concat(xmlFipersona).concat(xmlContenidoPer).concat(xmlFimultiValor).concat(xmlFiUltimo);
            //String xmlFinalMsg = xmlFiPriNumPers.concat(xmlFiPersCli).concat(xmlFiContenido).concat(xmlFimultiValor).concat(xmlFiUltimo);
            
            docmsg.setXmlMsg(xmlFinalMsg);
            this.LogDebug("PRUEBA PRE" + docmsg.getXmlMsg());
            }
                    
        }
        catch (Exception Ex) {
            this.setError(this.getClass().getName(), Ex.getLocalizedMessage());
            ProducidoError = true;
        }
        return ProducidoError;
    }
    
}
