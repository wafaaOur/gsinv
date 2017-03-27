/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Commande;
import bean.CommandeItem;
import bean.Produit;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author the joker
 */
@Stateless
public class CommandeFacade extends AbstractFacade<Commande> {

    @PersistenceContext(unitName = "gestionStockInventairePU")
    private EntityManager em;
    
    private CommandeItem commandeItem;
    @EJB
    private CommandeItemFacade commandeItemFacade;
    private Produit produit;
    
    
     public void clone(Commande commandeSource,Commande commandeDestination){
        commandeDestination.setId(commandeSource.getId());
        commandeDestination.setDateCommande(commandeSource.getDateCommande());
        commandeDestination.setMontantTotal(commandeSource.getMontantTotal());
    }
   
    public Commande clone(Commande commande){
        Commande cloned=new Commande();
        clone(commande, cloned);
        return cloned;
    }
   
    
    
    
    public List<Commande> findById(Long id) {
        return em.createQuery("SELECT c FROM Commande c WHERE c.id= '" + id + "'").getResultList();
    }
   
    public List<Commande>findByFournisseur(String cin){
        return em.createQuery("SELECT f FROM Commande f WHERE f.fournisseur.cin='" +cin+ "'").getResultList();
    }
    
   public void save(Commande commande,List<CommandeItem>commandeItems){
       Double montantTotal=0D;
       commande.setId(generateId("Commande", "id"));
       create(commande);
       for(CommandeItem commandeItem:commandeItems){
           montantTotal+=commandeItem.getTotale()*commandeItem.getQuantite();
           commandeItem.setCommande(commande);
           commandeItemFacade.create(commandeItem);
          
       }
       
       commande.setMontantTotal(montantTotal);
       edit(commande);
   }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CommandeFacade() {
        super(Commande.class);
    }

    public CommandeItemFacade getCommandeItemFacade() {
        return commandeItemFacade;
    }

    public void setCommandeItemFacade(CommandeItemFacade commandeItemFacade) {
        this.commandeItemFacade = commandeItemFacade;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }
    
}
