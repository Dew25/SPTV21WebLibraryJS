

package session;

import entity.History;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Stateless
public class HistoryFacade extends AbstractFacade<History> {

    @PersistenceContext(unitName = "SPTV21WebLibraryJSPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public HistoryFacade() {
        super(History.class);
    }
    
    public List<History> findReadingBooks() {
        try {
            return em.createQuery("SELECT h FROM History h WHERE h.returnBook = null")
                    .getResultList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
