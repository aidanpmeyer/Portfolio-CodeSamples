using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class bullet : MonoBehaviour
{
    public float bulletTimer;
    public float bulletDamage;
    private Rigidbody2D bulletRB;
    public float movespeed;
    // Start is called before the first frame update
    void Awake()
    {
        bulletRB = GetComponent<Rigidbody2D>();
    }

    public void shoot (Vector2 vect){
        bulletRB.velocity = vect.normalized * movespeed;
    }

    // Update is called once per frame
    void Update()
    {
        if (bulletTimer > 0){
            bulletTimer -= Time.deltaTime;
        }
        else{
             Destroy(this.gameObject);
        }
        
    }

    private void OnTriggerEnter2D(Collider2D collision)
    {
        if(collision.transform.CompareTag("Player"))
        {
            collision.transform.GetComponent<PlayerControler>().TakeDamage(bulletDamage);
            Destroy(this.gameObject);
        }
    }
}
