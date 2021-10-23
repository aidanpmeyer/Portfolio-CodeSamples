using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Experimental.Rendering.Universal;

public class Candle : Possessable
{
    
    #region candleVariables
    private List<KeyCode> keycodes;
    private Light2D candleLight;
    public bool lit;
    private Color color0 = new Color(0xFF, 0x7E, 0xA0);
    private Color color1 = new Color(0x00, 0xE0, 0xFF);
    #endregion
    // Start is called before the first frame update
    void Start()
    {
        candleLight = this.GetComponent<Light2D>();
    
        if(lit){
            Light();
        } 
    }

    // Update is called once per frame
    void Update()
    {
           
        int numGhosts = ghostList.Count;
        if (numGhosts == 2) {
            Light();
            List<KeyCode> g1keys = ghostList[0].getPlayerKeyCodes();
            List<KeyCode> g2keys = ghostList[1].getPlayerKeyCodes();
            //Add the depossess keys
            //keycodes[6] (E)
            keycodes[6] = g1keys[6];
            //keycodes[7] (U)
            keycodes[7] = g2keys[6];
            

        } else if (numGhosts == 1) {
            if(!lit){
                 if (ghostList[0].player1){
                    BlueLight();
                } else {
                    RedLight();
                }
            }
           
            keycodes = new List<KeyCode>(8);
            List<KeyCode> ghostCodes = ghostList[0].getPlayerKeyCodes();
            foreach (KeyCode x in ghostCodes) {
                keycodes.Add(x);
            }
            //Any Random Non-Used Keycode should work
            keycodes.Add(KeyCode.Mouse6);
        }
            
        if (numGhosts >= 1){
        
            if (Input.GetKeyDown(keycodes[6]) && !firstEntry) {
                //Debug.Log("Ghost: Pressed Leave Key");
                if(!lit){
                    Off();
                }
                base.Depossess(ghostList[0].player1);
                ghostList.Remove(ghostList[0]);
                numGhosts--;
            }

            if (numGhosts == 2 && Input.GetKeyDown(keycodes[7]) && !firstEntry) {
                //Debug.Log("Second Ghost: Pressed Leave Key");
                if(!lit){
                    Off();
                }
                base.Depossess(ghostList[1].player1);
                ghostList.Remove(ghostList[1]);
                numGhosts--;
                lit = true;
            }
            
        }
        
        if (firstEntry){
            firstEntry = !firstEntry;
        }
    }

    #region lightFunctions
    private void Light(){
        //Debug.Log("Lit");
        candleLight.color = new Color(0xEC, 0xE6, 0x97);
        candleLight.intensity = 0.004f;
        lit = true;

    }

    private void RedLight(){
        candleLight.intensity = 0.004f;
        candleLight.color = color0;
        Debug.Log("red light on");
    }

    private void BlueLight(){
        candleLight.intensity = 0.004f;
        candleLight.color = color1;
        Debug.Log("blue light on");
        
    }

    private void Off(){
        candleLight.intensity = 0;
    }
    #endregion
}
