using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PlayerController : MonoBehaviour {
    public Rigidbody2D playerRigidbody;
    public SpriteRenderer SR;

	// initialization
	void Start () {
        playerRigidbody = GetComponent<Rigidbody2D>();
        SR = GetComponent<SpriteRenderer>();
	}
	
	// Update is called once per frame
	void Update () {
        float moveHorizontal = Input.GetAxis("Horizontal");
        float moveVertical = Input.GetAxis("Vertical");

        Vector2 movement = new Vector2(moveHorizontal, moveVertical);
        playerRigidbody.velocity = movement * 2;

        if(Input.GetKeyDown(KeyCode.F)) {
            StartCoroutine(Death());
        }
	}

    void OnCollisionEnter2D(Collision2D other)
    {
        if (other.gameObject.tag == "Wall") {
            playerRigidbody.velocity = Vector2.zero;
        }
    }

    IEnumerator Death() {
        float elapsedTime = 0f;
        float TotalTime = 3.0f;
        Debug.Log(SR.color.GetType());
        while (SR.color != Color.black) {
            SR.color = Color.Lerp(SR.color, Color.black, elapsedTime/ TotalTime);
            elapsedTime+= Time.deltaTime;
            yield return null;    
        }
        
    }

}