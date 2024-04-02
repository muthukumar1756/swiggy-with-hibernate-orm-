package org.swiggy.restaurant.internal.dao.version2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.swiggy.common.hibernate.QueryBuilder;
import org.swiggy.common.hibernate.SessionBuilder;

import org.swiggy.common.hibernate.SessionHandler;
import org.swiggy.common.hibernate.TransactionHandler;

import org.swiggy.restaurant.internal.dao.RestaurantDAO;
import org.swiggy.restaurant.model.Food;
import org.swiggy.restaurant.model.Restaurant;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RestaurantDAOImpl implements RestaurantDAO {

    private static final Logger LOGGER = LogManager.getLogger(RestaurantDAOImpl.class);
    private static RestaurantDAOImpl restaurantDAOImpl;
    private SessionBuilder sessionBuilder;

    private RestaurantDAOImpl() {
        sessionBuilder = SessionBuilder.getSessionBuilder().getSessionFactory();
    }

    /**
     * <p>
     * Gets the object of the restaurant database implementation class.
     * </p>
     *
     * @return The restaurant database service implementation object
     */
    public static RestaurantDAOImpl getInstance() {
        if (null == restaurantDAOImpl) {
            return restaurantDAOImpl = new RestaurantDAOImpl();
        }

        return restaurantDAOImpl;
    }

    /**
     * {@inheritDoc}
     *
     * @param restaurant Represents the restaurant
     * @return True if restaurant profile is created, false otherwise
     */
    @Override
    public boolean createRestaurantProfile(final Restaurant restaurant) {
        Optional<TransactionHandler> transaction = Optional.empty();

        try (final SessionHandler session = sessionBuilder.buildSession()) {
            transaction = Optional.of(session.getTransaction());

            transaction.get().begin();
            session.save(restaurant);
            transaction.get().commit();

            return true;
        } catch (Exception message) {
            if (transaction.isPresent()) {
                transaction.get().rollBack();
            }
            LOGGER.warn(message.getMessage());

            return false;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param restaurantDataType Represents the type of data of the restaurant
     * @param restaurantData Represents the data of the restaurant
     * @param password Represents the password of the restaurant
     * @return The restaurant object
     */
    @Override
    public Optional<Restaurant> getRestaurant(final String restaurantDataType, final String restaurantData, final String password) {
        final String query = """
        select id, name, phone_number, email_id, password from restaurant where :type = :restaurantData and
        password = :password""";
        Optional<TransactionHandler> transaction = Optional.empty();

        try (final SessionHandler session = sessionBuilder.buildSession()) {
            transaction = Optional.of(session.getTransaction());

            transaction.get().begin();
            final QueryBuilder queryBuilder = session.createQuery(query, Restaurant.class);

            queryBuilder.setParameter("type", restaurantDataType);
            queryBuilder.setParameter("restaurantData", restaurantData);
            queryBuilder.setParameter("password", password);
            final Restaurant restaurant = (Restaurant) queryBuilder.getSingleResult();

            transaction.get().commit();

            if (null != restaurant) {
                return Optional.of(restaurant);
            }
        } catch (Exception message) {
            if (transaction.isPresent()) {
                transaction.get().rollBack();
            }
            LOGGER.warn(message.getMessage());
        }

        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     *
     * @param restaurantId Represents the id of the restaurant
     * @return The restaurant object
     */
    @Override
    public Optional<Restaurant> getRestaurantById(final long restaurantId) {
        Optional<TransactionHandler> transaction = Optional.empty();

        try (final SessionHandler session = sessionBuilder.buildSession()) {
            transaction = Optional.of(session.getTransaction());

            transaction.get().begin();
            final Restaurant restaurant = session.get(Restaurant.class, restaurantId);
            transaction.get().commit();

            if (null != restaurant) {
                return Optional.of(restaurant);
            }
        } catch (Exception message) {
            if (transaction.isPresent()) {
                transaction.get().rollBack();
            }
            LOGGER.warn(message.getMessage());
        }

        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     *
     * @return The list of all restaurants
     */
    @Override
    public Optional<List<Restaurant>> getRestaurants() {
        final String query = "from restaurant";
        Optional<TransactionHandler> transaction = Optional.empty();

        try (final SessionHandler session = sessionBuilder.buildSession()) {
            transaction = Optional.of(session.getTransaction());

            transaction.get().begin();
            final QueryBuilder queryBuilder = session.createQuery(query, Restaurant.class);
            final List<Restaurant> restaurantList = queryBuilder.executeQuery();

            transaction.get().commit();

            if (!restaurantList.isEmpty()) {
                return Optional.of(restaurantList);
            }
        } catch (Exception message) {
            if (transaction.isPresent()) {
                transaction.get().rollBack();
            }
            LOGGER.warn(message.getMessage());
        }

        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     *
     * @param restaurants Represents list of restaurants
     */
    @Override
    public boolean loadRestaurantList(final List<Restaurant> restaurants) {
        Optional<TransactionHandler> transaction = Optional.empty();

        try (final SessionHandler session = sessionBuilder.buildSession()) {
            transaction = Optional.of(session.getTransaction());

            transaction.get().begin();

            for (final Restaurant restaurant : restaurants) {
                session.save(restaurant);
            }
            transaction.get().commit();

            return true;
        } catch(Exception message) {
            if (transaction.isPresent()) {
                transaction.get().rollBack();
            }
            LOGGER.warn(message.getMessage());
        }

        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @param food Represents the food added by the restaurant
     * @param restaurantId Represents the id of the restaurant
     * @return True if food is added, false otherwise
     */
    @Override
    public boolean addFood(final Food food, final long restaurantId) {
        Optional<TransactionHandler> transaction = Optional.empty();

        try (final SessionHandler session = sessionBuilder.buildSession()) {
            transaction = Optional.of(session.getTransaction());

            transaction.get().begin();
            session.save(food);
            mapFoodsWithRestaurant(food.getId(), restaurantId);
            transaction.get().commit();

            return true;
        } catch (Exception message) {
            if (transaction.isPresent()) {
                transaction.get().rollBack();
            }
            LOGGER.warn(message.getMessage());
        }

        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @param menuCard Contains the list of foods from the restaurant
     */
    @Override
    public void loadMenuCard(final Map<Food, Long> menuCard) {
        Optional<TransactionHandler> transaction = Optional.empty();

        try (final SessionHandler session = sessionBuilder.buildSession()) {
            transaction = Optional.of(session.getTransaction());

            transaction.get().begin();

            for (final Map.Entry<Food, Long> restaurantFood : menuCard.entrySet()) {
                final Food food = restaurantFood.getKey();
                final Long restaurantId = restaurantFood.getValue();

                session.save(food);
                mapFoodsWithRestaurant(food.getId(), restaurantId);
            }
            transaction.get().commit();
        } catch (Exception message) {
            if (transaction.isPresent()) {
                transaction.get().rollBack();
            }
            LOGGER.warn(message.getMessage());
        }
    }

    /**
     * <p>
     * Maps the food with restaurant.
     * </p>
     *
     * @param foodId Represents the id of the food
     * @param restaurantId Represents the id of the restaurant
     */
    private void mapFoodsWithRestaurant(final long foodId, final long restaurantId) {
        final String query = "insert into restaurant_food (food_id, restaurant_id) values(:foodId, :restaurantId)";
        Optional<TransactionHandler> transaction = Optional.empty();

        try (final SessionHandler session = sessionBuilder.buildSession()) {
            transaction = Optional.of(session.getTransaction());

            transaction.get().begin();
            final QueryBuilder queryBuilder = session.createQuery(query);

            queryBuilder.setParameter("foodId", foodId);
            queryBuilder.setParameter("restaurantId", restaurantId);
            queryBuilder.executeUpdate();
        } catch (Exception message) {
            if (transaction.isPresent()) {
                transaction.get().rollBack();
            }
            LOGGER.warn(message.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param foodId Represents the id of the food
     * @return Available quantity of food from the restaurant
     */
    @Override
    public Optional<Integer> getFoodQuantity(final long foodId) {
        final String query = "select food_quantity from food where id = :foodId";
        Optional<TransactionHandler> transaction = Optional.empty();

        try (final SessionHandler session = sessionBuilder.buildSession()) {
            transaction = Optional.of(session.getTransaction());

            transaction.get().begin();
            final QueryBuilder queryBuilder = session.createQuery(query, Integer.class);

            queryBuilder.setParameter("foodId", foodId);
            final int quantity = (Integer) queryBuilder.getSingleResult();

            transaction.get().commit();

            return Optional.of(quantity) ;
        } catch (Exception message) {
            if (transaction.isPresent()) {
                transaction.get().rollBack();
            }
            LOGGER.warn(message.getMessage());
        }

        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     *
     * @param restaurantId Represents the id of the Restaurant
     * @param foodTypeId Represents the id of the food type.
     * @return The list of menucard having foods
     */
    @Override
    public Optional<List<Food>> getMenuCard(final long restaurantId, final int foodTypeId) {
        final String query = """
                select f.id, f.name, f.rate, f.type, f.quantity from food f
                 join restaurant_food rf on f.id = rf.food_id
                 join restaurant r on rf.restaurant_id = r.id
                 where r.id = :restaurantId and f.food_type in (:veg, :nonveg)""";
        Optional<TransactionHandler> transaction = Optional.empty();

        try (final SessionHandler session = sessionBuilder.buildSession()) {
            transaction = Optional.of(session.getTransaction());

            transaction.get().begin();
            final QueryBuilder queryBuilder = session.createQuery(query, Food.class);

            queryBuilder.setParameter("restaurantId", restaurantId);

            if (1 == foodTypeId) {
                queryBuilder.setParameter("veg", 1);
                queryBuilder.setParameter("nonveg", - 1);
            } else if (2 == foodTypeId) {
                queryBuilder.setParameter("veg", 2);
                queryBuilder.setParameter("nonveg", - 1);
            } else {
                queryBuilder.setParameter("veg", 1);
                queryBuilder.setParameter("nonveg", 2);
            }
            final List<Food> menuCard = queryBuilder.executeQuery();

            transaction.get().commit();

            if (!menuCard.isEmpty()) {
                return Optional.of(menuCard);
            }
        } catch (Exception message) {
            if (transaction.isPresent()) {
                transaction.get().rollBack();
            }
            LOGGER.warn(message.getMessage());
        }

        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     *
     * @param foodId Represents the id of the food
     * @return True if food is removed, false otherwise
     */
    @Override
    public boolean removeFood(final long foodId) {
        final String query = "delete from food where id = :foodId";
        Optional<TransactionHandler> transaction = Optional.empty();

        try (final SessionHandler session = sessionBuilder.buildSession()) {
            transaction = Optional.of(session.getTransaction());

            transaction.get().begin();
            final QueryBuilder queryBuilder = session.createQuery(query);
            final int result  = queryBuilder.executeUpdate();

            transaction.get().commit();

            return 0 < result;
        } catch (Exception message) {
            if (transaction.isPresent()) {
                transaction.get().rollBack();
            }
            LOGGER.warn(message.getMessage());
        }

        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @param restaurantId Represents the id of the restaurant
     * @param restaurantData Represents the data of the restaurant to be updated
     * @param type Represents the type of data of the restaurant to be updated
     * @return True if data is updated, false otherwise
     */
    @Override
    public boolean updateRestaurantData(final long restaurantId, final String type, final String restaurantData) {
        final String query = "update restaurant set :type = :restaurantData where id = :restaurantId";
        Optional<TransactionHandler> transaction = Optional.empty();

        try (final SessionHandler session = sessionBuilder.buildSession()) {
            transaction = Optional.of(session.getTransaction());

            transaction.get().begin();
            final QueryBuilder queryBuilder = session.createQuery(query, Restaurant.class);

            queryBuilder.setParameter("type", type);
            queryBuilder.setParameter("restaurantData", restaurantData);
            queryBuilder.setParameter("restaurantId", restaurantId);
            final long result = queryBuilder.executeUpdate();

            transaction.get().commit();

            return 0 < result;
        } catch (Exception message) {
            if (transaction.isPresent()) {
                transaction.get().rollBack();
            }
            LOGGER.warn(message.getMessage());
        }

        return false;
    }
}