package de.uniks.stp24.model.game;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Map;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;

public class Jobs
{
   public static final String PROPERTY_CREATED_AT = "createdAt";
   public static final String PROPERTY_UPDATED_AT = "updatedAt";
   public static final String PROPERTY__ID = "_id";
   public static final String PROPERTY_PROGRESS = "progress";
   public static final String PROPERTY_TOTAL = "total";
   public static final String PROPERTY_TYPE = "type";
   public static final String PROPERTY_BUILDING = "building";
   public static final String PROPERTY_DISTRICT = "district";
   public static final String PROPERTY_TECHNOLOGY = "technology";
   public static final String PROPERTY_COST = "cost";
   public static final String PROPERTY_EMPIRE = "empire";
   public static final String PROPERTY_FRACTION = "fraction";
   public static final String PROPERTY_PRIORITY = "priority";
   public static final String PROPERTY_RESULT = "result";
   public static final String PROPERTY_FLEET = "fleet";
   public static final String PROPERTY_PATH = "path";
   public static final String PROPERTY_SHIP = "ship";
   private LocalDateTime createdAt;
   private LocalDateTime updatedAt;
   private String _id;
   private double progress;
   private double total;
   private String type;
   private String building;
   private String district;
   private String technology;
   private Map<String, Integer> cost;
   private Empire empire;
   protected PropertyChangeSupport listeners;
   private Fraction fraction;
   private int priority;
   private de.uniks.stp24.dto.ResultDto result;
   private Fleet fleet;
   private List<Fraction> path;
   private String ship;

   public LocalDateTime getCreatedAt()
   {
      return this.createdAt;
   }

   public Jobs setCreatedAt(LocalDateTime value)
   {
      if (Objects.equals(value, this.createdAt))
      {
         return this;
      }

      final LocalDateTime oldValue = this.createdAt;
      this.createdAt = value;
      this.firePropertyChange(PROPERTY_CREATED_AT, oldValue, value);
      return this;
   }

   public LocalDateTime getUpdatedAt()
   {
      return this.updatedAt;
   }

   public Jobs setUpdatedAt(LocalDateTime value)
   {
      if (Objects.equals(value, this.updatedAt))
      {
         return this;
      }

      final LocalDateTime oldValue = this.updatedAt;
      this.updatedAt = value;
      this.firePropertyChange(PROPERTY_UPDATED_AT, oldValue, value);
      return this;
   }

   public String get_id()
   {
      return this._id;
   }

   public Jobs set_id(String value)
   {
      if (Objects.equals(value, this._id))
      {
         return this;
      }

      final String oldValue = this._id;
      this._id = value;
      this.firePropertyChange(PROPERTY__ID, oldValue, value);
      return this;
   }

   public double getProgress()
   {
      return this.progress;
   }

   public Jobs setProgress(double value)
   {
      if (value == this.progress)
      {
         return this;
      }

      final double oldValue = this.progress;
      this.progress = value;
      this.firePropertyChange(PROPERTY_PROGRESS, oldValue, value);
      return this;
   }

   public double getTotal()
   {
      return this.total;
   }

   public Jobs setTotal(double value)
   {
      if (value == this.total)
      {
         return this;
      }

      final double oldValue = this.total;
      this.total = value;
      this.firePropertyChange(PROPERTY_TOTAL, oldValue, value);
      return this;
   }

   public String getType()
   {
      return this.type;
   }

   public Jobs setType(String value)
   {
      if (Objects.equals(value, this.type))
      {
         return this;
      }

      final String oldValue = this.type;
      this.type = value;
      this.firePropertyChange(PROPERTY_TYPE, oldValue, value);
      return this;
   }

   public String getBuilding()
   {
      return this.building;
   }

   public Jobs setBuilding(String value)
   {
      if (Objects.equals(value, this.building))
      {
         return this;
      }

      final String oldValue = this.building;
      this.building = value;
      this.firePropertyChange(PROPERTY_BUILDING, oldValue, value);
      return this;
   }

   public String getDistrict()
   {
      return this.district;
   }

   public Jobs setDistrict(String value)
   {
      if (Objects.equals(value, this.district))
      {
         return this;
      }

      final String oldValue = this.district;
      this.district = value;
      this.firePropertyChange(PROPERTY_DISTRICT, oldValue, value);
      return this;
   }

   public String getTechnology()
   {
      return this.technology;
   }

   public Jobs setTechnology(String value)
   {
      if (Objects.equals(value, this.technology))
      {
         return this;
      }

      final String oldValue = this.technology;
      this.technology = value;
      this.firePropertyChange(PROPERTY_TECHNOLOGY, oldValue, value);
      return this;
   }

   public Map<String, Integer> getCost()
   {
      return this.cost;
   }

   public Jobs setCost(Map<String, Integer> value)
   {
      if (Objects.equals(value, this.cost))
      {
         return this;
      }

      final Map<String, Integer> oldValue = this.cost;
      this.cost = value;
      this.firePropertyChange(PROPERTY_COST, oldValue, value);
      return this;
   }

   public Empire getEmpire()
   {
      return this.empire;
   }

   public Jobs setEmpire(Empire value)
   {
      if (this.empire == value)
      {
         return this;
      }

      final Empire oldValue = this.empire;
      if (this.empire != null)
      {
         this.empire = null;
         oldValue.withoutJobs(this);
      }
      this.empire = value;
      if (value != null)
      {
         value.withJobs(this);
      }
      this.firePropertyChange(PROPERTY_EMPIRE, oldValue, value);
      return this;
   }

   public Fraction getFraction()
   {
      return this.fraction;
   }

   public Jobs setFraction(Fraction value)
   {
      if (this.fraction == value)
      {
         return this;
      }

      final Fraction oldValue = this.fraction;
      if (this.fraction != null)
      {
         this.fraction = null;
         oldValue.withoutJobs(this);
      }
      this.fraction = value;
      if (value != null)
      {
         value.withJobs(this);
      }
      this.firePropertyChange(PROPERTY_FRACTION, oldValue, value);
      return this;
   }

   public Jobs setPriority(int value)
   {
      if (value == this.priority)
      {
         return this;
      }

      final int oldValue = this.priority;
      this.priority = value;
      this.firePropertyChange(PROPERTY_PRIORITY, oldValue, value);
      return this;
   }

   public de.uniks.stp24.dto.ResultDto getResult()
   {
      return this.result;
   }

   public Jobs setResult(de.uniks.stp24.dto.ResultDto value)
   {
      if (Objects.equals(value, this.result))
      {
         return this;
      }

      final de.uniks.stp24.dto.ResultDto oldValue = this.result;
      this.result = value;
      this.firePropertyChange(PROPERTY_RESULT, oldValue, value);
      return this;
   }

   public Fleet getFleet()
   {
      return this.fleet;
   }

   public Jobs setFleet(Fleet value)
   {
      if (this.fleet == value)
      {
         return this;
      }

      final Fleet oldValue = this.fleet;
      if (this.fleet != null)
      {
         this.fleet = null;
         oldValue.setMoving(null);
      }
      this.fleet = value;
      if (value != null)
      {
         value.setMoving(this);
      }
      this.firePropertyChange(PROPERTY_FLEET, oldValue, value);
      return this;
   }

   public List<Fraction> getPath()
   {
      return this.path != null ? Collections.unmodifiableList(this.path) : Collections.emptyList();
   }

   public Jobs withPath(Fraction value)
   {
      if (this.path == null)
      {
         this.path = new CopyOnWriteArrayList<>();
      }
      if (!this.path.contains(value))
      {
         this.path.add(value);
         value.withCurrentTraveling(this);
         this.firePropertyChange(PROPERTY_PATH, null, value);
      }
      return this;
   }

   public Jobs withoutPath(Fraction value)
   {
      if (this.path != null && this.path.remove(value))
      {
         value.withoutCurrentTraveling(this);
         this.firePropertyChange(PROPERTY_PATH, value, null);
      }
      return this;
   }

   public Jobs withoutPath(Collection<? extends Fraction> value)
   {
      for (final Fraction item : value)
      {
         this.withoutPath(item);
      }
      return this;
   }

   public String getShip()
   {
      return this.ship;
   }

   public Jobs setShip(String value)
   {
      if (Objects.equals(value, this.ship))
      {
         return this;
      }

      final String oldValue = this.ship;
      this.ship = value;
      this.firePropertyChange(PROPERTY_SHIP, oldValue, value);
      return this;
   }

   public int getPriority()
   {
      return this.priority;
   }

   public Jobs withPath(Fraction... value)
   {
      for (final Fraction item : value)
      {
         this.withPath(item);
      }
      return this;
   }

   public Jobs withPath(Collection<? extends Fraction> value)
   {
      for (final Fraction item : value)
      {
         this.withPath(item);
      }
      return this;
   }

   public Jobs withoutPath(Fraction... value)
   {
      for (final Fraction item : value)
      {
         this.withoutPath(item);
      }
      return this;
   }

   public boolean firePropertyChange(String propertyName, Object oldValue, Object newValue)
   {
      if (this.listeners != null)
      {
         this.listeners.firePropertyChange(propertyName, oldValue, newValue);
         return true;
      }
      return false;
   }

   public PropertyChangeSupport listeners()
   {
      if (this.listeners == null)
      {
         this.listeners = new PropertyChangeSupport(this);
      }
      return this.listeners;
   }

   @Override
   public String toString()
   {
      final StringBuilder result = new StringBuilder();
      result.append(' ').append(this.get_id());
      result.append(' ').append(this.getType());
      result.append(' ').append(this.getBuilding());
      result.append(' ').append(this.getDistrict());
      result.append(' ').append(this.getTechnology());
      result.append(' ').append(this.getShip());
      return result.substring(1);
   }

   public void removeYou()
   {
      this.setFleet(null);
      this.setEmpire(null);
      this.setFraction(null);
      this.withoutPath(new ArrayList<>(this.getPath()));
   }
}
